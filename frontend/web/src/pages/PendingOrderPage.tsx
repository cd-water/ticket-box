import { useEffect, useState } from 'react'
import dayjs from 'dayjs'
import { ArrowLeft } from 'lucide-react'
import { Link, useParams } from 'react-router'
import { Badge } from '@/components/ui/badge'
import { Button } from '@/components/ui/button'
import {
  Empty,
  EmptyDescription,
  EmptyHeader,
  EmptyTitle,
} from '@/components/ui/empty'
import { Skeleton } from '@/components/ui/skeleton'
import { useOrderDetail } from '@/hooks/useBookingQueries'
import { formatShowDate, formatTime } from '@/lib/date'

const STATUS_TEXT: Record<number, string> = {
  0: '待支付',
  1: '已支付',
  2: '已取消',
  3: '已超时',
}

export default function PendingOrderPage() {
  const params = useParams()
  const orderNo = params.orderId ?? ''
  const { data, isPending, isError, refetch } = useOrderDetail(orderNo)
  const [remaining, setRemaining] = useState(0)

  useEffect(() => {
    if (!data) return
    const tick = () => {
      const seconds = Math.max(0, dayjs(data.expireTime).diff(dayjs(), 'second'))
      setRemaining(seconds)
      if (seconds === 0 && data.status === 0) {
        void refetch()
      }
    }
    tick()
    const timer = window.setInterval(tick, 1000)
    return () => window.clearInterval(timer)
  }, [data, refetch])

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
            <EmptyTitle>订单不存在</EmptyTitle>
            <EmptyDescription>检查订单号或返回影院重新购票</EmptyDescription>
          </EmptyHeader>
          <Button variant="outline" nativeButton={false} render={<Link to="/cinema" />}>
            返回影院
          </Button>
        </Empty>
      </div>
    )
  }

  const minutes = Math.floor(remaining / 60)
  const seconds = remaining % 60
  const expired = data.status === 3 || (data.status === 0 && remaining === 0)

  return (
    <div className="mx-auto max-w-2xl space-y-6">
      <Button variant="ghost" size="sm" nativeButton={false} render={<Link to="/cinema" />}>
        <ArrowLeft />
        返回影院
      </Button>

      <section className="rounded-xl border border-border bg-card p-6">
        <div className="flex flex-wrap items-center justify-between gap-3">
          <div>
            <h1 className="text-2xl font-semibold text-foreground">{data.movieTitle}</h1>
            <p className="mt-1 text-sm text-muted-foreground">
              {data.cinemaName} · {data.hallName}
            </p>
          </div>
          <Badge variant={expired ? 'destructive' : 'secondary'}>
            {expired ? '已超时' : STATUS_TEXT[data.status] ?? '未知状态'}
          </Badge>
        </div>

        <div className="mt-5 space-y-2 text-sm">
          <p>
            <span className="text-muted-foreground">场次：</span>
            {formatShowDate(data.startTime)} {formatTime(data.startTime)}-{formatTime(data.endTime)}
          </p>
          <p>
            <span className="text-muted-foreground">座位：</span>
            {data.seats.map((seat) => `${seat.rowNum}排${seat.colNum}座`).join('、')}
          </p>
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
            <span className="text-2xl font-semibold text-primary">¥{data.totalAmount.toFixed(2)}</span>
          </div>
        </div>

        {!expired && data.status === 0 ? (
          <p className="mt-4 text-sm text-muted-foreground">
            请在 {minutes}:{String(seconds).padStart(2, '0')} 内完成支付
          </p>
        ) : null}
        {expired ? <p className="mt-4 text-sm text-destructive">订单已超时，座位已释放。</p> : null}

        <div className="mt-6 flex gap-3">
          <Button size="lg" className="h-10 flex-1" disabled>
            去支付（支付功能待接入）
          </Button>
        </div>
      </section>
    </div>
  )
}
