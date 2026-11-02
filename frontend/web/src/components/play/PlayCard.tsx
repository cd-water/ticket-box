import { MapPin } from 'lucide-react'
import { Link } from 'react-router'
import type { PlayListItem } from '@/api/play'
import { Badge } from '@/components/ui/badge'

export function PlayCard({ item }: { item: PlayListItem }) {
  return (
    <Link
      to={`/play/${item.id}`}
      className="group overflow-hidden rounded-xl border border-border bg-card transition-colors hover:border-primary/60"
    >
      <div className="aspect-[16/10] overflow-hidden bg-muted">
        {item.cover ? (
          <img
            src={item.cover}
            alt={item.title}
            loading="lazy"
            className="size-full object-cover transition-transform duration-300 group-hover:scale-105"
          />
        ) : null}
      </div>
      <div className="space-y-2 p-4">
        <div className="flex items-start justify-between gap-2">
          <h3 className="truncate font-semibold text-foreground">{item.title}</h3>
          <Badge variant="secondary">余 {item.stock}</Badge>
        </div>
        <p className="flex items-center gap-1 text-sm text-muted-foreground">
          <MapPin className="size-4 shrink-0" />
          <span className="truncate">{item.address}</span>
        </p>
        <p className="text-lg font-semibold text-primary">¥{item.price.toFixed(2)}</p>
      </div>
    </Link>
  )
}
