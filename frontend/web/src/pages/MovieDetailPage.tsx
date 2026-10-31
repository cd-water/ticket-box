import { ArrowLeft } from 'lucide-react'
import { Link, useParams } from 'react-router'
import { Avatar, AvatarFallback, AvatarImage } from '@/components/ui/avatar'
import { Badge } from '@/components/ui/badge'
import { Button } from '@/components/ui/button'
import {
  Empty,
  EmptyDescription,
  EmptyHeader,
  EmptyTitle,
} from '@/components/ui/empty'
import { Skeleton } from '@/components/ui/skeleton'
import { ReviewSection } from '@/components/movie/ReviewSection'
import { useMovieDetail } from '@/hooks/useMovieQueries'
import { formatDateTime } from '@/lib/date'
import type { Crew } from '@/api/movie'

function CrewList({
  label,
  crews,
  showRole = false,
}: {
  label: string
  crews: Crew[]
  showRole?: boolean
}) {
  return (
    <div className="flex flex-wrap items-center gap-3">
      <span className="text-sm text-muted-foreground">{label}：</span>
      {crews.length === 0 ? (
        <span className="text-sm">暂无</span>
      ) : (
        crews.map((crew) => (
          <span key={crew.personId} className="flex items-center gap-1.5 text-sm">
            <Avatar size="sm">
              <AvatarImage src={crew.avatar} alt={crew.name} />
              <AvatarFallback>{crew.name.slice(0, 1)}</AvatarFallback>
            </Avatar>
            {crew.name}
            {showRole && crew.roleName ? (
              <span className="text-muted-foreground">饰 {crew.roleName}</span>
            ) : null}
          </span>
        ))
      )}
    </div>
  )
}

export default function MovieDetailPage() {
  const params = useParams()
  const movieId = Number(params.id)
  const { data, isPending, isError } = useMovieDetail(movieId)

  if (Number.isFinite(movieId) && movieId > 0 && isPending) {
    return (
      <div className="grid gap-8 md:grid-cols-[240px_1fr]">
        <Skeleton className="aspect-[2/3] w-full max-w-[240px] rounded-xl" />
        <div className="space-y-4">
          <Skeleton className="h-9 w-2/3" />
          <Skeleton className="h-4 w-1/3" />
          <Skeleton className="h-24 w-full" />
        </div>
      </div>
    )
  }

  if (!Number.isFinite(movieId) || movieId <= 0 || isError || !data) {
    return (
      <div className="flex min-h-[50vh] items-center justify-center">
        <Empty className="p-0">
          <EmptyHeader>
            <EmptyTitle className="text-lg">电影不存在或已下架</EmptyTitle>
            <EmptyDescription>返回电影页看看其他影片</EmptyDescription>
          </EmptyHeader>
          <Button variant="outline" nativeButton={false} render={<Link to="/movie" />}>
            <ArrowLeft />
            返回电影
          </Button>
        </Empty>
      </div>
    )
  }

  return (
    <div className="space-y-10">
      <Button variant="ghost" size="sm" nativeButton={false} render={<Link to="/movie" />}>
        <ArrowLeft />
        返回电影
      </Button>

      <section className="grid gap-8 md:grid-cols-[240px_1fr]">
        <img
          src={data.poster}
          alt={data.title}
          className="aspect-[2/3] w-full max-w-[240px] rounded-xl object-cover"
        />
        <div className="space-y-4">
          <div className="flex flex-wrap items-center gap-3">
            <h1 className="text-3xl font-bold tracking-tight text-foreground">{data.title}</h1>
            <Badge variant="secondary">{data.tab === 'showing' ? '热映中' : '即将上映'}</Badge>
          </div>

          <p className="text-sm text-muted-foreground">
            {formatDateTime(data.releaseDate)} · {data.duration} 分钟
          </p>

          <Button
            size="lg"
            className="h-10 w-fit px-6"
            nativeButton={false}
            render={<Link to={`/cinema?movie=${data.id}`} />}
          >
            选座购票
          </Button>

          <div className="flex flex-wrap gap-2">
            {data.genres.map((genre) => (
              <Badge key={genre.id} variant="outline">
                {genre.name}
              </Badge>
            ))}
          </div>

          <CrewList label="导演" crews={data.directors} />
          <CrewList label="演员" crews={data.actors} showRole />

          <p className="leading-7 text-foreground/90">{data.synopsis}</p>
        </div>
      </section>

      <ReviewSection movieId={data.id} reviewCount={data.reviewCount} />
    </div>
  )
}
