import { useEffect, useState } from 'react'
import { useNavigate, useParams } from 'react-router'
import type { Seat } from '@/api/cinema'
import { SeatMap } from '@/components/booking/SeatMap'
import { Badge } from '@/components/ui/badge'
import { Button } from '@/components/ui/button'
import {
  Empty,
  EmptyDescription,
  EmptyHeader,
  EmptyTitle,
} from '@/components/ui/empty'
import { Skeleton } from '@/components/ui/skeleton'
import { toast } from '@/components/ui/toast'
import { useCreateOrder } from '@/hooks/useBookingQueries'
import { useSeatMap } from '@/hooks/useCinemaQueries'
import { formatShowDate, formatTime } from '@/lib/date'

const MAX_SEATS = 6

export default function SeatSelectionPage() {
  const params = useParams()
  const navigate = useNavigate()
  const screeningId = Number(params.screeningId)
  const { data, isPending, isError } = useSeatMap(screeningId)
  const createOrder = useCreateOrder()
  const [selected, setSelected] = useState<Set<number>>(new Set())

  useEffect(() => {
    setSelected(new Set())
  }, [screeningId])

  function toggleSeat(seat: Seat) {
    if (seat.status !== 0 || createOrder.isPending) return
    setSelected((current) => {
      const next = new Set(current)
      if (next.has(seat.seatId)) {
        next.delete(seat.seatId)
        return next
      }
      if (next.size >= MAX_SEATS) {
        toast.add({ title: `最多选择 ${MAX_SEATS} 个座位`, type: 'warning' })
        return current
      }
      next.add(seat.seatId)
      return next
    })
  }

  function confirm() {
    if (!data || selected.size === 0) return
    createOrder.mutate(
      { screeningId: data.screeningId, seatIds: [...selected] },
      {
        onSuccess: (order) => navigate(`/order/${order.orderNo}`),
        onError: () => setSelected(new Set()),
      },
    )
  }

  if (isPending) {
    return (
      <div className="space-y-6">
        <Skeleton className="h-20 w-full rounded-lg" />
        <Skeleton className="h-[420px] w-full rounded-lg" />
      </div>
    )
  }

  if (isError || !data) {
    return (
      <div className="flex min-h-[50vh] items-center justify-center">
        <Empty className="p-0">
          <EmptyHeader>
            <EmptyTitle>排场不存在或已取消</EmptyTitle>
            <EmptyDescription>返回影院重新选择</EmptyDescription>
          </EmptyHeader>
          <Button variant="outline" onClick={() => navigate('/cinema')}>
            返回影院
          </Button>
        </Empty>
      </div>
    )
  }

  const selectedSeats = data.seats.filter((seat) => selected.has(seat.seatId))
  const total = data.price * selectedSeats.length

  return (
    <div className="space-y-6 pb-28">
      <header className="rounded-xl border border-border bg-card p-5">
        <div className="flex flex-wrap items-center gap-3">
          <h1 className="text-xl font-semibold text-foreground">{data.movieTitle}</h1>
          <Badge variant="secondary">{data.hallName}</Badge>
        </div>
        <p className="mt-2 text-sm text-muted-foreground">
          {data.cinemaName} · {formatShowDate(data.startTime)} {formatTime(data.startTime)}-{formatTime(data.endTime)}
        </p>
        <p className="mt-1 text-sm text-muted-foreground">票价 ¥{data.price.toFixed(2)} / 座</p>
      </header>

      <SeatMap
        rowCount={data.rowCount}
        colCount={data.colCount}
        seats={data.seats}
        selected={selected}
        onToggle={toggleSeat}
      />

      <div className="fixed inset-x-0 bottom-0 z-40 border-t border-border bg-background/95 backdrop-blur">
        <div className="mx-auto flex w-[min(1180px,calc(100vw-48px))] flex-wrap items-center justify-between gap-3 py-3">
          <div className="min-w-0">
            <p className="truncate text-sm text-muted-foreground">
              已选：{selectedSeats.length > 0
                ? selectedSeats.map((seat) => `${seat.rowNum}排${seat.colNum}座`).join('、')
                : '请选择座位'}
            </p>
            <p className="text-lg font-semibold text-foreground">¥{total.toFixed(2)}</p>
          </div>
          <Button
            size="lg"
            className="h-10 px-6"
            disabled={selectedSeats.length === 0 || createOrder.isPending}
            onClick={confirm}
          >
            {createOrder.isPending ? '提交中…' : '确认选座'}
          </Button>
        </div>
      </div>

      {selectedSeats.length === 0 ? (
        <p className="text-center text-xs text-muted-foreground">最多可选 {MAX_SEATS} 个座位</p>
      ) : null}
    </div>
  )
}
