import type { Screening } from '@/api/cinema'
import { Badge } from '@/components/ui/badge'
import { Button } from '@/components/ui/button'
import { formatShowDate, formatTime } from '@/lib/date'

export function ScreeningRow({ screening, onSelect }: { screening: Screening; onSelect: () => void }) {
  return (
    <div className="flex flex-col gap-3 rounded-lg border border-border bg-card p-4 md:flex-row md:items-center md:justify-between">
      <div className="space-y-1">
        <div className="flex items-baseline gap-2">
          <span className="text-lg font-semibold text-foreground">{formatTime(screening.startTime)}</span>
          <span className="text-sm text-muted-foreground">散场 {formatTime(screening.endTime)}</span>
          <Badge variant="outline">{screening.hallName}</Badge>
        </div>
        <p className="text-xs text-muted-foreground">{formatShowDate(screening.startTime)}</p>
      </div>
      <div className="flex items-center gap-4">
        <div className="text-right">
          <p className="text-lg font-semibold text-primary">¥{screening.price.toFixed(2)}</p>
          <p className="text-xs text-muted-foreground">余票 {screening.availableSeats}</p>
        </div>
        <Button onClick={onSelect} disabled={screening.availableSeats === 0}>
          选座购票
        </Button>
      </div>
    </div>
  )
}
