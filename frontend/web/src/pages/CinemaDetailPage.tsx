import { useNavigate, useParams, useSearchParams } from 'react-router'
import { MapPin, Phone } from 'lucide-react'
import { CinemaMovieRail } from '@/components/cinema/CinemaMovieRail'
import { ScreeningRow } from '@/components/cinema/ScreeningRow'
import { Button } from '@/components/ui/button'
import {
  Empty,
  EmptyDescription,
  EmptyHeader,
  EmptyTitle,
} from '@/components/ui/empty'
import { Skeleton } from '@/components/ui/skeleton'
import { useCinemaDetail, useScreenings } from '@/hooks/useCinemaQueries'

export default function CinemaDetailPage() {
  const params = useParams()
  const [searchParams, setSearchParams] = useSearchParams()
  const navigate = useNavigate()
  const cinemaId = Number(params.cinemaId)
  const detail = useCinemaDetail(cinemaId)
  const selectedMovieId = Number(searchParams.get('movie')) || detail.data?.movies[0]?.id || 0
  const screenings = useScreenings(cinemaId, selectedMovieId)

  function selectMovie(movieId: number) {
    const next = new URLSearchParams(searchParams)
    next.set('movie', String(movieId))
    setSearchParams(next, { replace: true })
  }

  if (Number.isFinite(cinemaId) && cinemaId > 0 && detail.isPending) {
    return (
      <div className="space-y-4">
        <Skeleton className="h-9 w-1/2" />
        <Skeleton className="h-4 w-1/3" />
        <div className="flex gap-3 overflow-hidden">
          {Array.from({ length: 6 }).map((_, index) => (
            <Skeleton key={index} className="h-40 w-28 shrink-0 rounded-lg" />
          ))}
        </div>
      </div>
    )
  }

  if (!Number.isFinite(cinemaId) || cinemaId <= 0 || detail.isError || !detail.data) {
    return (
      <div className="flex min-h-[50vh] items-center justify-center">
        <Empty className="p-0">
          <EmptyHeader>
            <EmptyTitle>影院不存在或已停用</EmptyTitle>
            <EmptyDescription>返回影院列表看看其他影院</EmptyDescription>
          </EmptyHeader>
          <Button variant="outline" onClick={() => navigate('/cinema')}>
            返回影院
          </Button>
        </Empty>
      </div>
    )
  }

  return (
    <div className="space-y-7">
      <header className="space-y-2">
        <h1 className="text-2xl font-semibold text-foreground">{detail.data.name}</h1>
        <p className="flex items-center gap-1.5 text-sm text-muted-foreground">
          <MapPin className="size-4" />
          {detail.data.address || '暂无地址'}
        </p>
        <p className="flex items-center gap-1.5 text-sm text-muted-foreground">
          <Phone className="size-4" />
          {detail.data.phone || '暂无电话'}
        </p>
      </header>

      {detail.data.movies.length === 0 ? (
        <Empty className="p-0">
          <EmptyHeader>
            <EmptyTitle>暂无上映电影</EmptyTitle>
            <EmptyDescription>换一家影院看看</EmptyDescription>
          </EmptyHeader>
        </Empty>
      ) : (
        <>
          <CinemaMovieRail
            movies={detail.data.movies}
            selectedId={selectedMovieId}
            onSelect={selectMovie}
          />

          {screenings.isPending ? (
            <div className="space-y-3">
              {Array.from({ length: 3 }).map((_, index) => (
                <Skeleton key={index} className="h-24 w-full rounded-lg" />
              ))}
            </div>
          ) : null}

          {screenings.isError ? (
            <div className="py-10 text-center">
              <p className="text-sm text-muted-foreground">排场加载失败</p>
              <Button className="mt-3" variant="outline" size="sm" onClick={() => void screenings.refetch()}>
                重试
              </Button>
            </div>
          ) : null}

          {screenings.data && screenings.data.length === 0 ? (
            <div className="py-10">
              <Empty className="p-0">
                <EmptyHeader>
                  <EmptyTitle>暂无排场</EmptyTitle>
                  <EmptyDescription>该电影暂未安排场次</EmptyDescription>
                </EmptyHeader>
              </Empty>
            </div>
          ) : null}

          {screenings.data && screenings.data.length > 0 ? (
            <div className="space-y-3">
              {screenings.data.map((screening) => (
                <ScreeningRow
                  key={screening.id}
                  screening={screening}
                  onSelect={() => navigate(`/screening/${screening.id}/seats`)}
                />
              ))}
            </div>
          ) : null}
        </>
      )}
    </div>
  )
}
