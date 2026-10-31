import type { Seat } from '@/api/cinema'
import { Button } from '@/components/ui/button'

interface SeatMapProps {
  rowCount: number
  colCount: number
  seats: Seat[]
  selected: Set<number>
  onToggle: (seat: Seat) => void
}

export function SeatMap({ rowCount, colCount, seats, selected, onToggle }: SeatMapProps) {
  const byCell = new Map(seats.map((seat) => [`${seat.rowNum}-${seat.colNum}`, seat]))
  const rows = Array.from({ length: rowCount }, (_, index) => index + 1)
  const cols = Array.from({ length: colCount }, (_, index) => index + 1)

  return (
    <div className="overflow-x-auto">
      <div className="mx-auto w-fit space-y-1.5">
        <div className="mb-6 text-center">
          <div className="mx-auto h-1.5 w-3/4 rounded-full bg-muted" />
          <p className="mt-2 text-xs text-muted-foreground">银幕</p>
        </div>
        {rows.map((row) => (
          <div key={row} className="flex items-center gap-2">
            <span className="w-5 shrink-0 text-right text-xs text-muted-foreground">{row}</span>
            <div className="flex gap-1.5">
              {cols.map((col) => {
                const seat = byCell.get(`${row}-${col}`)
                if (!seat) {
                  return <span key={col} className="size-8" />
                }
                const isSelected = selected.has(seat.seatId)
                const disabled = seat.status !== 0
                return (
                  <Button
                    key={seat.seatId}
                    type="button"
                    size="icon-sm"
                    variant={isSelected ? 'default' : 'outline'}
                    disabled={disabled}
                    aria-label={`${row}排${col}座`}
                    aria-pressed={isSelected}
                    title={seat.status === 1 ? '已锁定' : seat.status === 2 ? '已售' : undefined}
                    className="size-8"
                    onClick={() => onToggle(seat)}
                  >
                    {col}
                  </Button>
                )
              })}
            </div>
          </div>
        ))}
      </div>
    </div>
  )
}
