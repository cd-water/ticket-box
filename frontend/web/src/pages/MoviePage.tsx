import { useEffect, useState } from 'react'
import { useSearchParams } from 'react-router'
import { RotateCcw, Search } from 'lucide-react'
import type { MovieTab } from '@/api/movie'
import { MovieCard } from '@/components/movie/MovieCard'
import { MovieGridSkeleton } from '@/components/movie/MovieGridSkeleton'
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
import { Tabs, TabsList, TabsTrigger } from '@/components/ui/tabs'
import { useMovieList } from '@/hooks/useMovieQueries'

const PAGE_SIZE = 12

function pageItems(totalPages: number, current: number): Array<number | 'ellipsis'> {
  if (totalPages <= 7) {
    return Array.from({ length: totalPages }, (_, index) => index + 1)
  }
  const items: Array<number | 'ellipsis'> = [1]
  if (current > 4) items.push('ellipsis')
  const start = Math.max(2, current - 1)
  const end = Math.min(totalPages - 1, current + 1)
  for (let page = start; page <= end; page += 1) items.push(page)
  if (current < totalPages - 3) items.push('ellipsis')
  items.push(totalPages)
  return items
}

export default function MoviePage() {
  const [params, setParams] = useSearchParams()
  const tab: MovieTab = params.get('tab') === 'upcoming' ? 'upcoming' : 'showing'
  const keyword = params.get('keyword') ?? ''
  const page = Math.max(1, Number(params.get('page') ?? 1) || 1)
  const [searchText, setSearchText] = useState(keyword)

  useEffect(() => {
    setSearchText(keyword)
  }, [keyword])

  const { data, isPending, isError, refetch, isFetching } = useMovieList(tab, keyword, page)
  const totalPages = data ? Math.max(1, Math.ceil(data.total / PAGE_SIZE)) : 1

  function update(next: Record<string, string>) {
    const search = new URLSearchParams(params)
    Object.entries(next).forEach(([key, value]) => search.set(key, value))
    setParams(search)
  }

  function applyKeyword(raw: string) {
    const value = raw.trim()
    const next = new URLSearchParams(params)
    if (value) next.set('keyword', value)
    else next.delete('keyword')
    next.set('page', '1')
    setParams(next)
  }

  function resetSearch() {
    setSearchText('')
    applyKeyword('')
  }

  function changePage(nextPage: number) {
    if (nextPage < 1 || nextPage > totalPages) return
    update({ page: String(nextPage) })
    window.scrollTo({ top: 0, behavior: 'smooth' })
  }

  return (
    <div className="space-y-6">
      <div className="flex flex-col gap-4 md:flex-row md:items-center md:justify-between">
        <Tabs
          className="w-full md:w-[360px]"
          value={tab}
          onValueChange={(value) => {
            const next = new URLSearchParams(params)
            next.set('tab', String(value))
            next.set('page', '1')
            setParams(next)
          }}
        >
          <TabsList className="w-full">
            <TabsTrigger value="showing">热映电影</TabsTrigger>
            <TabsTrigger value="upcoming">待映电影</TabsTrigger>
          </TabsList>
        </Tabs>

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
            placeholder="搜索电影名称"
            aria-label="搜索电影名称"
            maxLength={64}
            className="h-10 flex-1 bg-card md:w-56 md:flex-none"
          />
          <Button type="submit" className="h-10 shrink-0">
            <Search />
            搜索
          </Button>
          <Button
            type="button"
            variant="outline"
            className="h-10 shrink-0"
            onClick={resetSearch}
          >
            <RotateCcw />
            重置
          </Button>
        </form>
      </div>

      {isPending ? <MovieGridSkeleton /> : null}

      {isError ? (
        <div className="flex min-h-[40vh] items-center justify-center">
          <div className="text-center">
            <p className="text-muted-foreground">电影加载失败</p>
            <Button className="mt-4" variant="outline" onClick={() => void refetch()}>
              重试
            </Button>
          </div>
        </div>
      ) : null}

      {data && data.records.length === 0 ? (
        <div className="flex min-h-[40vh] items-center justify-center">
          <Empty className="p-0">
            <EmptyHeader>
              <EmptyTitle className="text-lg">暂无电影</EmptyTitle>
              <EmptyDescription>换个关键词试试</EmptyDescription>
            </EmptyHeader>
          </Empty>
        </div>
      ) : null}

      {data && data.records.length > 0 ? (
        <>
          <div
            className={`grid grid-cols-2 gap-x-4 gap-y-6 sm:grid-cols-3 md:grid-cols-4 lg:grid-cols-6 ${
              isFetching ? 'opacity-70' : ''
            }`}
          >
            {data.records.map((movie) => (
              <MovieCard key={movie.id} movie={movie} />
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
                {pageItems(totalPages, page).map((item, index) =>
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
