import type { ReactNode } from 'react'
import { ArrowRight } from 'lucide-react'
import { Link } from 'react-router'
import { BannerCarousel } from '@/components/home/BannerCarousel'
import { MovieCard } from '@/components/movie/MovieCard'
import { PlayCard } from '@/components/play/PlayCard'
import { ShowCard } from '@/components/show/ShowCard'
import { Button } from '@/components/ui/button'
import { Skeleton } from '@/components/ui/skeleton'
import { useHome } from '@/hooks/useHomeQueries'

function Section({
  title,
  moreTo,
  children,
}: {
  title: string
  moreTo: string
  children: ReactNode
}) {
  return (
    <section className="space-y-4">
      <div className="flex items-center justify-between">
        <h2 className="text-xl font-semibold text-foreground">{title}</h2>
        <Link
          to={moreTo}
          className="flex items-center gap-1 text-sm text-muted-foreground transition-colors hover:text-primary"
        >
          更多
          <ArrowRight className="size-4" />
        </Link>
      </div>
      {children}
    </section>
  )
}

export default function HomePage() {
  const { data, isPending, isError, refetch } = useHome()

  if (isPending) {
    return (
      <div className="space-y-10">
        <Skeleton className="aspect-[21/7] w-full rounded-xl" />
        <div className="space-y-4">
          <Skeleton className="h-7 w-32" />
          <div className="grid grid-cols-2 gap-4 sm:grid-cols-4 lg:grid-cols-8">
            {Array.from({ length: 8 }).map((_, index) => (
              <Skeleton key={index} className="aspect-[2/3] w-full rounded-lg" />
            ))}
          </div>
        </div>
        <div className="space-y-4">
          <Skeleton className="h-7 w-24" />
          <div className="grid gap-4 sm:grid-cols-2 lg:grid-cols-4">
            {Array.from({ length: 4 }).map((_, index) => (
              <Skeleton key={index} className="h-36 w-full rounded-xl" />
            ))}
          </div>
        </div>
      </div>
    )
  }

  if (isError || !data) {
    return (
      <div className="flex min-h-[50vh] items-center justify-center">
        <div className="text-center">
          <p className="text-muted-foreground">首页加载失败</p>
          <Button className="mt-4" variant="outline" onClick={() => void refetch()}>
            重试
          </Button>
        </div>
      </div>
    )
  }

  return (
    <div className="space-y-12">
      {data.banners.length > 0 ? <BannerCarousel banners={data.banners} /> : null}

      {data.movies.length > 0 ? (
        <Section title="热门电影" moreTo="/movie">
          <div className="grid grid-cols-2 gap-x-4 gap-y-6 sm:grid-cols-4 lg:grid-cols-8">
            {data.movies.map((movie) => (
              <MovieCard key={movie.id} movie={movie} />
            ))}
          </div>
        </Section>
      ) : null}

      {data.shows.length > 0 ? (
        <Section title="热门演出" moreTo="/show">
          <div className="grid gap-4 sm:grid-cols-2 lg:grid-cols-4">
            {data.shows.map((show) => (
              <ShowCard key={show.id} show={show} />
            ))}
          </div>
        </Section>
      ) : null}

      {data.plays.length > 0 ? (
        <Section title="玩乐精选" moreTo="/play">
          <div className="grid gap-4 sm:grid-cols-2 lg:grid-cols-4">
            {data.plays.map((item) => (
              <PlayCard key={item.id} item={item} />
            ))}
          </div>
        </Section>
      ) : null}
    </div>
  )
}
