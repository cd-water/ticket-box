import { CalendarDays, MapPin } from 'lucide-react'
import { Link } from 'react-router'
import type { ShowListItem } from '@/api/show'
import { formatShowDate, formatTime } from '@/lib/date'

export function ShowCard({ show }: { show: ShowListItem }) {
  return (
    <Link
      to={`/show/${show.id}`}
      className="group flex gap-3 rounded-xl border border-border bg-card p-3 transition-colors hover:border-primary/60"
    >
      <div className="aspect-[3/4] w-24 shrink-0 overflow-hidden rounded-lg bg-muted">
        {show.poster ? (
          <img
            src={show.poster}
            alt={show.title}
            loading="lazy"
            className="size-full object-cover transition-transform duration-300 group-hover:scale-105"
          />
        ) : null}
      </div>
      <div className="min-w-0 flex-1 space-y-1.5">
        <h3 className="truncate font-semibold text-foreground">{show.title}</h3>
        <p className="flex items-center gap-1 text-sm text-muted-foreground">
          <MapPin className="size-4 shrink-0" />
          <span className="truncate">{show.venueName}</span>
        </p>
        <p className="flex items-center gap-1 text-sm text-muted-foreground">
          <CalendarDays className="size-4 shrink-0" />
          {formatShowDate(show.startTime)} {formatTime(show.startTime)}
        </p>
        <p className="pt-1 text-sm font-semibold text-primary">
          {show.minPrice != null ? `¥${show.minPrice.toFixed(2)} 起` : '暂无可售票档'}
        </p>
      </div>
    </Link>
  )
}
