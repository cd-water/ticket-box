import { useState } from 'react'
import type { ReviewSort } from '@/api/movie'
import { Button } from '@/components/ui/button'
import {
  Empty,
  EmptyDescription,
  EmptyHeader,
  EmptyTitle,
} from '@/components/ui/empty'
import { Skeleton } from '@/components/ui/skeleton'
import { Tabs, TabsList, TabsTrigger } from '@/components/ui/tabs'
import { useReviews } from '@/hooks/useMovieQueries'
import { ReviewComposer } from '@/components/movie/ReviewComposer'
import { ReviewItem } from '@/components/movie/ReviewItem'

interface ReviewSectionProps {
  movieId: number
  reviewCount: number
}

export function ReviewSection({ movieId, reviewCount }: ReviewSectionProps) {
  const [sort, setSort] = useState<ReviewSort>('hot')
  const query = useReviews(movieId, sort)
  const reviews = query.data?.pages.flatMap((page) => page.records) ?? []

  return (
    <section className="border-t border-border pt-8">
      <div className="mb-4 flex flex-wrap items-center justify-between gap-3">
        <h2 className="text-xl font-semibold text-foreground">全部影评 {reviewCount}</h2>
        <Tabs value={sort} onValueChange={(value) => setSort(value as ReviewSort)}>
          <TabsList>
            <TabsTrigger value="hot">最热</TabsTrigger>
            <TabsTrigger value="latest">最新</TabsTrigger>
          </TabsList>
        </Tabs>
      </div>

      <ReviewComposer movieId={movieId} />

      <div className="mt-6 divide-y divide-border">
        {query.isPending
          ? Array.from({ length: 3 }).map((_, index) => (
              <div key={index} className="flex gap-3 py-4">
                <Skeleton className="size-8 rounded-full" />
                <div className="flex-1 space-y-2">
                  <Skeleton className="h-3 w-24" />
                  <Skeleton className="h-4 w-full" />
                  <Skeleton className="h-4 w-2/3" />
                </div>
              </div>
            ))
          : null}

        {query.isError ? (
          <div className="py-8 text-center">
            <p className="text-sm text-muted-foreground">影评加载失败</p>
            <Button className="mt-3" variant="outline" size="sm" onClick={() => void query.refetch()}>
              重试
            </Button>
          </div>
        ) : null}

        {!query.isPending && !query.isError && reviews.length === 0 ? (
          <div className="py-10">
            <Empty className="p-0">
              <EmptyHeader>
                <EmptyTitle>还没有影评</EmptyTitle>
                <EmptyDescription>来抢第一个沙发</EmptyDescription>
              </EmptyHeader>
            </Empty>
          </div>
        ) : null}

        {reviews.map((review) => (
          <ReviewItem key={review.id} movieId={movieId} review={review} />
        ))}
      </div>

      {query.hasNextPage ? (
        <div className="mt-2 flex justify-center">
          <Button
            variant="outline"
            disabled={query.isFetchingNextPage}
            onClick={() => void query.fetchNextPage()}
          >
            {query.isFetchingNextPage ? '加载中…' : '查看更多影评'}
          </Button>
        </div>
      ) : reviews.length > 0 ? (
        <p className="mt-4 text-center text-xs text-muted-foreground">没有更多了</p>
      ) : null}
    </section>
  )
}
