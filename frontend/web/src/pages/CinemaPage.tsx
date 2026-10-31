import { useEffect, useState } from 'react'
import { useSearchParams } from 'react-router'
import { RotateCcw, Search } from 'lucide-react'
import { CinemaCard } from '@/components/cinema/CinemaCard'
import { Button } from '@/components/ui/button'
import {
  Empty,
  EmptyDescription,
  EmptyHeader,
  EmptyTitle,
} from '@/components/ui/empty'
import { Input } from '@/components/ui/input'
import {
  Pagination,
  PaginationContent,
  PaginationEllipsis,
  PaginationItem,
  PaginationLink,
  PaginationNext,
  PaginationPrevious,
} from '@/components/ui/pagination'
import { Skeleton } from '@/components/ui/skeleton'
import { useCinemaList } from '@/hooks/useCinemaQueries'
import { useMovieDetail } from '@/hooks/useMovieQueries'
import { getPageItems } from '@/lib/pagination'

const PAGE_SIZE = 10

export default function CinemaPage() {
  const [params, setParams] = useSearchParams()
  const name = params.get('name') ?? ''
  const page = Math.max(1, Number(params.get('page') ?? 1) || 1)
  const movieId = Number(params.get('movie')) || 0
  const [searchText, setSearchText] = useState(name)
  const { data: movie } = useMovieDetail(movieId)
  const { data, isPending, isError, refetch, isFetching } = useCinemaList(
    name,
    page,
    movieId || undefined,
  )
  const totalPages = data ? Math.max(1, Math.ceil(data.total / PAGE_SIZE)) : 1

  useEffect(() => {
    setSearchText(name)
  }, [name])

  function applyKeyword(raw: string) {
    const value = raw.trim()
    const next = new URLSearchParams(params)
    if (value) next.set('name', value)
    else next.delete('name')
    next.set('page', '1')
    setParams(next)
  }

  function resetSearch() {
    setSearchText('')
    applyKeyword('')
  }

  function changePage(nextPage: number) {
    if (nextPage < 1 || nextPage > totalPages) return
    const next = new URLSearchParams(params)
    next.set('page', String(nextPage))
    setParams(next)
    window.scrollTo({ top: 0, behavior: 'smooth' })
  }

  return (
    <div className="space-y-6">
      <div className="flex flex-col gap-3 md:flex-row md:items-center md:justify-between">
        <div>
          <h1 className="text-2xl font-semibold text-foreground">影院</h1>
          {movieId > 0 ? (
            <p className="mt-1 text-sm text-muted-foreground">
              《{movie?.title ?? '该影片'}》可购票影院
            </p>
          ) : null}
        </div>
        <form
          className="flex w-full gap-2 md:w-auto"
          onSubmit={(event) => {
            event.preventDefault()
            applyKeyword(searchText)
          }}
        >
          <Input
            value={searchText}
            onChange={(event) => setSearchText(event.target.value)}
            placeholder="搜索影院名称"
            aria-label="搜索影院名称"
            maxLength={64}
            className="h-10 flex-1 bg-card md:w-56 md:flex-none"
          />
          <Button type="submit" className="h-10 shrink-0">
            <Search />
            搜索
          </Button>
          <Button type="button" variant="outline" className="h-10 shrink-0" onClick={resetSearch}>
            <RotateCcw />
            重置
          </Button>
        </form>
      </div>

      {isPending ? (
        <div className="space-y-3">
          {Array.from({ length: 4 }).map((_, index) => (
            <Skeleton key={index} className="h-28 w-full rounded-xl" />
          ))}
        </div>
      ) : null}

      {isError ? (
        <div className="py-16 text-center">
          <p className="text-muted-foreground">影院加载失败</p>
          <Button className="mt-4" variant="outline" onClick={() => void refetch()}>
            重试
          </Button>
        </div>
      ) : null}

      {data && data.records.length === 0 ? (
        <div className="py-16">
          <Empty className="p-0">
            <EmptyHeader>
              <EmptyTitle>{movieId > 0 ? '暂无放映影院' : '暂无影院'}</EmptyTitle>
              <EmptyDescription>
                {movieId > 0 ? '该影片暂无未来场次' : '换个名称试试'}
              </EmptyDescription>
            </EmptyHeader>
          </Empty>
        </div>
      ) : null}

      {data && data.records.length > 0 ? (
        <>
          <div className={`space-y-3 ${isFetching ? 'opacity-70' : ''}`}>
            {data.records.map((cinema) => (
              <CinemaCard key={cinema.id} cinema={cinema} movieId={movieId || undefined} />
            ))}
          </div>

          {totalPages > 1 ? (
            <Pagination>
              <PaginationContent>
                <PaginationItem>
                  <PaginationPrevious
                    text="上一页"
                    href="#"
                    onClick={(event) => {
                      event.preventDefault()
                      changePage(page - 1)
                    }}
                  />
                </PaginationItem>
                {getPageItems(totalPages, page).map((item, index) =>
                  item === 'ellipsis' ? (
                    <PaginationItem key={`ellipsis-${index}`}>
                      <PaginationEllipsis />
                    </PaginationItem>
                  ) : (
                    <PaginationItem key={item}>
                      <PaginationLink
                        href="#"
                        isActive={item === page}
                        onClick={(event) => {
                          event.preventDefault()
                          changePage(item)
                        }}
                      >
                        {item}
                      </PaginationLink>
                    </PaginationItem>
                  ),
                )}
                <PaginationItem>
                  <PaginationNext
                    text="下一页"
                    href="#"
                    onClick={(event) => {
                      event.preventDefault()
                      changePage(page + 1)
                    }}
                  />
                </PaginationItem>
              </PaginationContent>
            </Pagination>
          ) : null}
        </>
      ) : null}
    </div>
  )
}
