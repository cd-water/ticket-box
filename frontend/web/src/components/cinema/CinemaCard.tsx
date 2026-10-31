import { MapPin, Phone } from 'lucide-react'
import { Link } from 'react-router'
import type { CinemaListItem } from '@/api/cinema'
import { Badge } from '@/components/ui/badge'

export function CinemaCard({ cinema }: { cinema: CinemaListItem }) {
  return (
    <Link
      to={`/cinema/${cinema.id}`}
      className="block rounded-xl border border-border bg-card p-5 transition-colors hover:border-primary/60"
    >
      <div className="flex items-center justify-between gap-3">
        <h3 className="text-lg font-semibold text-foreground">{cinema.name}</h3>
        <Badge variant="secondary">营业中</Badge>
      </div>
      <p className="mt-3 flex items-center gap-1.5 text-sm text-muted-foreground">
        <MapPin className="size-4" />
        {cinema.address || '暂无地址'}
      </p>
      <p className="mt-1 flex items-center gap-1.5 text-sm text-muted-foreground">
        <Phone className="size-4" />
        {cinema.phone || '暂无电话'}
      </p>
    </Link>
  )
}
