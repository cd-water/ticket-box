import { useEffect, useState } from 'react'
import dayjs from 'dayjs'
import { ArrowLeft } from 'lucide-react'
import { Link, useParams } from 'react-router'
import { Badge } from '@/components/ui/badge'
import { Button } from '@/components/ui/button'
import { ConfirmDialog } from '@/components/ui/confirm-dialog'
import {
  Empty,
  EmptyDescription,
  EmptyHeader,
  EmptyTitle,
} from '@/components/ui/empty'
import { Skeleton } from '@/components/ui/skeleton'
import { toast } from '@/components/ui/toast'
import {
  useCancelMovieOrder,
  useOrderDetail,
  usePayMovieOrder,
} from '@/hooks/useBookingQueries'
import {
  useCancelPlayOrder,
  useCancelShowOrder,
  usePayPlayOrder,
  usePayShowOrder,
  usePlayOrderDetail,
  useShowOrderDetail,
} from '@/hooks/useOrderQueries'
import { formatShowDate, formatTime } from '@/lib/date'

const STATUS_TEXT: Record<number, string> = {
  0: '待支付',
  1: '已支付',
  2: '已取消',
}

type OrderType = 'movie' | 'show' | 'play'

export default function OrderDetailPage() {
  const params = useParams()
  const type: OrderType =
    params.type === 'show' ? 'show' : params.type === 'play' ? 'play' : 'movie'
  const orderNo = params.orderNo ?? ''

  const movieQuery = useOrderDetail(type === 'movie' ? orderNo : '')
  const showQuery = useShowOrderDetail(type === 'show' ? orderNo : '')
  const playQuery = usePlayOrderDetail(type === 'play' ? orderNo : '')
  const { data, isPending, isError, refetch } =
    type === 'show' ? showQuery : type === 'play' ? playQuery : movieQuery

  const payMovie = usePayMovieOrder()
  const payShow = usePayShowOrder()
  const payPlay = usePayPlayOrder()
  const cancelMovie = useCancelMovieOrder()
  const cancelShow = useCancelShowOrder()
  const cancelPlay = useCancelPlayOrder()
  const [remaining, setRemaining] = useState<number | null>(null)
  const [confirmOpen, setConfirmOpen] = useState(false)

  const show = showQuery.data
  const play = playQuery.data
  const movie = movieQuery.data
  const expireTime = data?.expireTime ?? ''

  useEffect(() => {
    if (!data) return
    const tick = () => {
      const seconds = Math.max(0, dayjs(expireTime).diff(dayjs(), 'second'))
      setRemaining(seconds)
      if (seconds === 0 && data.status === 0) void refetch()
    }
    tick()
    const timer = window.setInterval(tick, 1000)
    return () => window.clearInterval(timer)
  }, [data, expireTime, refetch])

  if (isPending) {
    return (
      <div className="mx-auto max-w-2xl space-y-4">
        <Skeleton className="h-10 w-1/2" />
        <Skeleton className="h-64 w-full rounded-xl" />
      </div>
    )
  }

  if (isError || !data) {
    return (
      <div className="flex min-h-[50vh] items-center justify-center">
        <Empty className="p-0">
          <EmptyHeader>
            <EmptyTitle className="text-lg">订单不存在</EmptyTitle>
            <EmptyDescription>检查订单号或返回重新购买</EmptyDescription>
          </EmptyHeader>
          <Button
            variant="outline"
            nativeButton={false}
            render={
              <Link
                to={type === 'show' ? '/show' : type === 'play' ? '/play' : '/cinema'}
              />
            }
          >
            返回
          </Button>
        </Empty>
      </div>
    )
  }

  const status = data.status
  const pending = status === 0 && (remaining === null || remaining > 0)
  const expired = status === 0 && remaining === 0
  const minutes = Math.floor((remaining ?? 0) / 60)
  const seconds = (remaining ?? 0) % 60
  const title =
    type === 'show' ? show?.showTitle : type === 'play' ? play?.itemTitle : movie?.movieTitle
  const subtitle =
    type === 'show'
      ? `${show?.venueName ?? ''} · ${show?.venueAddress ?? ''}`
      : type === 'play'
        ? (play?.itemAddress ?? '')
        : `${movie?.cinemaName ?? ''} · ${movie?.hallName ?? ''}`
  const startTime =
    type === 'show' ? show?.startTime : type === 'play' ? undefined : movie?.startTime
  const backTo = type === 'show' ? '/show' : type === 'play' ? '/play' : '/cinema'

  function handlePay() {
    const onSuccess = () => toast.add({ title: '支付成功', type: 'success' })
    const onError = () => toast.add({ title: '支付失败，请稍后重试', type: 'error' })
    if (type === 'movie') payMovie.mutate(orderNo, { onSuccess, onError })
    else if (type === 'show') payShow.mutate(orderNo, { onSuccess, onError })
    else payPlay.mutate(orderNo, { onSuccess, onError })
  }

  function handleCancel() {
    const onSuccess = () => {
      setConfirmOpen(false)
      toast.add({ title: '订单已取消', type: 'info' })
    }
    const onError = () => setConfirmOpen(false)
    if (type === 'movie') cancelMovie.mutate(orderNo, { onSuccess, onError })
    else if (type === 'show') cancelShow.mutate(orderNo, { onSuccess, onError })
    else cancelPlay.mutate(orderNo, { onSuccess, onError })
  }

  const mutating =
    payMovie.isPending ||
    payShow.isPending ||
    payPlay.isPending ||
    cancelMovie.isPending ||
    cancelShow.isPending ||
    cancelPlay.isPending

  return (
    <div className="mx-auto max-w-2xl space-y-6">
      <Button
        variant="ghost"
        size="sm"
        nativeButton={false}
        render={<Link to={backTo} />}
      >
        <ArrowLeft />
        返回
      </Button>

      <section className="rounded-xl border border-border bg-card p-6">
        <div className="flex flex-wrap items-center justify-between gap-3">
          <div>
            <h1 className="text-2xl font-semibold text-foreground">{title}</h1>
            <p className="mt-1 text-sm text-muted-foreground">{subtitle}</p>
          </div>
          <Badge variant={status === 2 ? 'destructive' : 'secondary'}>
            {STATUS_TEXT[status] ?? '未知状态'}
          </Badge>
        </div>

        <div className="mt-5 space-y-2 text-sm">
          {startTime ? (
            <p>
              <span className="text-muted-foreground">场次：</span>
              {formatShowDate(startTime)} {formatTime(startTime)}
            </p>
          ) : null}
          {type === 'show' ? (
            <>
              <p>
                <span className="text-muted-foreground">票档：</span>
                {show?.ticketTypeName} · {show?.zone} · {show?.quantity} 张
              </p>
              <p>
                <span className="text-muted-foreground">座位：</span>
                {show?.seats.length
                  ? show.seats.map((seat) => `${seat.zone} ${seat.seatNo}号`).join('、')
                  : '支付后自动分配'}
              </p>
            </>
          ) : null}
          {type === 'play' ? (
            <p>
              <span className="text-muted-foreground">数量：</span>
              {play?.quantity} 张 · 单价 ¥{play?.unitPrice.toFixed(2)}
            </p>
          ) : null}
          {type === 'movie' ? (
            <p>
              <span className="text-muted-foreground">座位：</span>
              {movie?.seats.map((seat) => `${seat.rowNum}排${seat.colNum}座`).join('、')}
            </p>
          ) : null}
          <p>
            <span className="text-muted-foreground">订单号：</span>
            {data.orderNo}
          </p>
          <p>
            <span className="text-muted-foreground">下单时间：</span>
            {dayjs(data.createTime).format('YYYY-MM-DD HH:mm:ss')}
          </p>
        </div>

        <div className="mt-5 border-t border-border pt-5">
          <div className="flex items-center justify-between">
            <span className="text-sm text-muted-foreground">应付金额</span>
            <span className="text-2xl font-semibold text-primary">
              ¥{data.totalAmount.toFixed(2)}
            </span>
          </div>
        </div>

        {status === 0 && !expired ? (
          <p className="mt-4 text-sm text-muted-foreground">
            请在 {minutes}:{String(seconds).padStart(2, '0')} 内完成支付
          </p>
        ) : null}
        {expired ? <p className="mt-4 text-sm text-destructive">订单已关闭。</p> : null}

        {pending ? (
          <div className="mt-6 flex gap-3">
            <Button
              size="lg"
              className="h-10 flex-1"
              disabled={mutating}
              onClick={handlePay}
            >
              {payMovie.isPending || payShow.isPending || payPlay.isPending
                ? '支付中…'
                : '去支付'}
            </Button>
            <Button
              size="lg"
              variant="outline"
              className="h-10"
              disabled={mutating}
              onClick={() => setConfirmOpen(true)}
            >
              取消订单
            </Button>
          </div>
        ) : null}
      </section>

      <ConfirmDialog
        open={confirmOpen}
        onOpenChange={setConfirmOpen}
        title="取消订单"
        description={
          type === 'play'
            ? '取消后库存将被释放，且不可恢复。确定取消吗？'
            : '取消后这次购买的座位将被释放，且不可恢复。确定取消吗？'
        }
        confirmText="确认取消"
        onConfirm={handleCancel}
      />
    </div>
  )
}
