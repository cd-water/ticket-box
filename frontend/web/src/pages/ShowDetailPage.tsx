import { useState } from 'react'
import { ArrowLeft, CalendarDays, Clock, MapPin } from 'lucide-react'
import { Link, useNavigate, useParams } from 'react-router'
import { QuantityStepper } from '@/components/show/QuantityStepper'
import { Button } from '@/components/ui/button'
import {
  Empty,
  EmptyDescription,
  EmptyHeader,
  EmptyTitle,
} from '@/components/ui/empty'
import { Skeleton } from '@/components/ui/skeleton'
import { toast } from '@/components/ui/toast'
import { useCreateShowOrder, useShowDetail } from '@/hooks/useShowQueries'
import { formatShowDate, formatTime } from '@/lib/date'

export default function ShowDetailPage() {
  const params = useParams()
  const navigate = useNavigate()
  const showId = Number(params.id)
  const { data, isPending, isError } = useShowDetail(showId)
  const createOrder = useCreateShowOrder()
  const [ticketTypeId, setTicketTypeId] = useState<number | null>(null)
  const [quantity, setQuantity] = useState(1)

  if (Number.isFinite(showId) && showId > 0 && isPending) {
    return (
      <div className="grid gap-8 md:grid-cols-[260px_1fr]">
        <Skeleton className="aspect-[3/4] w-full max-w-[260px] rounded-xl" />
        <div className="space-y-4">
          <Skeleton className="h-9 w-2/3" />
          <Skeleton className="h-4 w-1/3" />
          <Skeleton className="h-24 w-full" />
        </div>
      </div>
    )
  }

  if (!Number.isFinite(showId) || showId <= 0 || isError || !data) {
    return (
      <div className="flex min-h-[50vh] items-center justify-center">
        <Empty className="p-0">
          <EmptyHeader>
            <EmptyTitle className="text-lg">演出不存在或已开演</EmptyTitle>
            <EmptyDescription>返回演出页看看其他场次</EmptyDescription>
          </EmptyHeader>
          <Button variant="outline" nativeButton={false} render={<Link to="/show" />}>
            <ArrowLeft />
            返回演出
          </Button>
        </Empty>
      </div>
    )
  }

  const detail = data
  const selected =
    detail.ticketTypes.find((type) => type.id === ticketTypeId && type.availableSeats > 0) ??
    detail.ticketTypes.find((type) => type.availableSeats > 0)
  const max = selected ? Math.min(6, selected.availableSeats) : 0
  const currentQuantity = Math.max(1, Math.min(quantity, max || 1))

  function selectTicket(id: number) {
    setTicketTypeId(id)
    setQuantity(1)
  }

  function buy() {
    if (!selected || max === 0) return
    createOrder.mutate(
      { showId: detail.id, ticketTypeId: selected.id, quantity: currentQuantity },
      {
        onSuccess: (order) => navigate(`/order/show/${order.orderNo}`),
        onError: () => toast.add({ title: '下单失败，请稍后重试', type: 'error' }),
      },
    )
  }

  return (
    <div className="space-y-8">
      <Button variant="ghost" size="sm" nativeButton={false} render={<Link to="/show" />}>
        <ArrowLeft />
        返回演出
      </Button>

      <section className="grid gap-8 md:grid-cols-[260px_1fr]">
        <img
          src={data.poster}
          alt={data.title}
          className="aspect-[3/4] w-full max-w-[260px] rounded-xl object-cover"
        />
        <div className="space-y-4">
          <h1 className="text-3xl font-semibold text-foreground">{data.title}</h1>
          <div className="space-y-2 text-sm text-muted-foreground">
            <p className="flex items-center gap-2">
              <MapPin className="size-4" />
              {data.venueName} · {data.venueAddress}
            </p>
            <p className="flex items-center gap-2">
              <CalendarDays className="size-4" />
              {formatShowDate(data.startTime)} {formatTime(data.startTime)}
            </p>
            <p className="flex items-center gap-2">
              <Clock className="size-4" />
              约 {data.duration} 分钟
            </p>
          </div>
          <p className="whitespace-pre-wrap text-sm leading-7 text-foreground/90">
            {data.description}
          </p>
        </div>
      </section>

      <section className="space-y-4">
        <h2 className="text-xl font-semibold text-foreground">选择票档</h2>
        <div className="grid gap-3 sm:grid-cols-2 lg:grid-cols-3">
          {data.ticketTypes.map((type) => {
            const active = selected?.id === type.id
            return (
              <button
                key={type.id}
                type="button"
                aria-pressed={active}
                disabled={type.availableSeats === 0}
                onClick={() => selectTicket(type.id)}
                className={`rounded-xl border p-4 text-left transition-colors disabled:cursor-not-allowed disabled:opacity-50 ${
                  active
                    ? 'border-primary bg-secondary'
                    : 'border-border bg-card hover:border-primary/60'
                }`}
              >
                <div className="flex items-center justify-between gap-2">
                  <span className="font-medium text-foreground">{type.name}</span>
                  <span className="text-lg font-semibold text-primary">
                    ¥{type.price.toFixed(2)}
                  </span>
                </div>
                <p className="mt-1 text-xs text-muted-foreground">
                  {type.zone} · 余票 {type.availableSeats}
                </p>
              </button>
            )
          })}
        </div>

        <div className="flex flex-wrap items-center gap-6 rounded-xl border border-border bg-card p-5">
          <div className="space-y-1">
            <p className="text-sm text-muted-foreground">购买数量</p>
            <QuantityStepper value={currentQuantity} max={max} onChange={setQuantity} />
          </div>
          <div className="space-y-1">
            <p className="text-sm text-muted-foreground">合计</p>
            <p className="text-2xl font-semibold text-primary">
              ¥{((selected?.price ?? 0) * currentQuantity).toFixed(2)}
            </p>
          </div>
          <Button
            size="lg"
            className="ml-auto h-10 px-8"
            disabled={!selected || max === 0 || createOrder.isPending}
            onClick={buy}
          >
            {createOrder.isPending ? '提交中…' : '立即购买'}
          </Button>
        </div>
        <p className="text-xs text-muted-foreground">
          支付成功后系统自动分配座位，同一订单优先连座。
        </p>
      </section>
    </div>
  )
}
