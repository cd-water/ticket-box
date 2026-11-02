import { useNavigate } from 'react-router'
import { Badge } from '@/components/ui/badge'
import { Button } from '@/components/ui/button'
import { Countdown } from '@/components/order/Countdown'
import { formatShowDate, formatTime } from '@/lib/date'

export type OrderType = 'movie' | 'show' | 'play'

export interface OrderCardData {
  orderNo: string
  status: number
  totalAmount: number
  expireTime: string
  createTime: string
  movieTitle?: string
  cinemaName?: string
  hallName?: string
  startTime?: string
  showTitle?: string
  venueName?: string
  venueAddress?: string
  itemTitle?: string
  itemAddress?: string
  unitPrice?: number
  quantity?: number
}

const STATUS_TEXT: Record<number, string> = {
  0: '待支付',
  1: '已支付',
  2: '已取消',
}

export function OrderListCard({
  type,
  order,
  busy,
  onPay,
  onCancel,
}: {
  type: OrderType
  order: OrderCardData
  busy: boolean
  onPay: () => void
  onCancel: () => void
}) {
  const navigate = useNavigate()
  const title =
    type === 'movie' ? order.movieTitle : type === 'show' ? order.showTitle : order.itemTitle
  const subtitle =
    type === 'movie'
      ? `${order.cinemaName ?? ''} · ${order.hallName ?? ''}`
      : type === 'show'
        ? (order.venueName ?? '')
        : (order.itemAddress ?? '')
  const time =
    type === 'play' || !order.startTime
      ? null
      : `${formatShowDate(order.startTime)} ${formatTime(order.startTime)}`

  return (
    <div
      role="button"
      tabIndex={0}
      onClick={() => navigate(`/order/${type}/${order.orderNo}`)}
      onKeyDown={(event) => {
        if (event.key === 'Enter' || event.key === ' ') {
          navigate(`/order/${type}/${order.orderNo}`)
        }
      }}
      className="cursor-pointer rounded-xl border border-border bg-card p-4 transition-colors hover:border-primary/60"
    >
      <div className="flex flex-wrap items-start justify-between gap-2">
        <div className="min-w-0">
          <h3 className="truncate font-semibold text-foreground">{title}</h3>
          <p className="mt-1 truncate text-sm text-muted-foreground">{subtitle}</p>
          {time ? <p className="mt-1 text-xs text-muted-foreground">{time}</p> : null}
          {type === 'play' ? (
            <p className="mt-1 text-xs text-muted-foreground">
              {order.quantity} 张 · 单价 ¥{order.unitPrice?.toFixed(2)}
            </p>
          ) : null}
        </div>
        <Badge variant={order.status === 2 ? 'destructive' : 'secondary'}>
          {STATUS_TEXT[order.status] ?? '未知状态'}
        </Badge>
      </div>

      <div className="mt-4 flex flex-wrap items-center justify-between gap-3">
        <div className="text-sm text-muted-foreground">
          <span className="mr-2">合计</span>
          <span className="text-lg font-semibold text-primary">
            ¥{order.totalAmount.toFixed(2)}
          </span>
          {order.status === 0 ? (
            <span className="ml-3">
              剩余 <Countdown expireTime={order.expireTime} />
            </span>
          ) : null}
        </div>
        {order.status === 0 ? (
          <div className="flex gap-2">
            <Button
              size="sm"
              disabled={busy}
              onClick={(event) => {
                event.stopPropagation()
                onPay()
              }}
            >
              去支付
            </Button>
            <Button
              size="sm"
              variant="outline"
              disabled={busy}
              onClick={(event) => {
                event.stopPropagation()
                onCancel()
              }}
            >
              取消订单
            </Button>
          </div>
        ) : null}
      </div>
    </div>
  )
}
