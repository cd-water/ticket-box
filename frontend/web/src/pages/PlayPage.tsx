import { useEffect, useState } from 'react'
import { useSearchParams } from 'react-router'
import { RotateCcw, Search } from 'lucide-react'
import { PlayCard } from '@/components/play/PlayCard'
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
import { usePlayList } from '@/hooks/usePlayQueries'
import { getPageItems } from '@/lib/pagination'

const PAGE_SIZE = 12

export default function PlayPage() {
  const [params, setParams] = useSearchParams()
  const keyword = params.get('keyword') ?? ''
  const page = Math.max(1, Number(params.get('page') ?? 1) || 1)
  const [searchText, setSearchText] = useState(keyword)

  useEffect(() => {
    setSearchText(keyword)
  }, [keyword])

  const { data, isPending, isError, refetch, isFetching } = usePlayList(keyword, page)
  const totalPages = data ? Math.max(1, Math.ceil(data.total / PAGE_SIZE)) : 1

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
    const next = new URLSearchParams(params)
    next.set('page', String(nextPage))
    setParams(next)
    window.scrollTo({ top: 0, behavior: 'smooth' })
  }

  return (
    <div className="space-y-6">
      <div className="flex flex-col gap-4 md:flex-row md:items-center md:justify-between">
        <h1 className="text-2xl font-semibold text-foreground">玩乐</h1>
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
            placeholder="搜索项目名称"
            aria-label="搜索项目名称"
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

      {isPending ? (
        <div className="grid gap-4 sm:grid-cols-2 lg:grid-cols-3">
          {Array.from({ length: 6 }).map((_, index) => (
            <Skeleton key={index} className="h-64 w-full rounded-xl" />
          ))}
        </div>
      ) : null}

      {isError ? (
        <div className="flex min-h-[40vh] items-center justify-center">
          <div className="text-center">
            <p className="text-muted-foreground">玩乐项目加载失败</p>
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
              <EmptyTitle className="text-lg">暂无玩乐项目</EmptyTitle>
              <EmptyDescription>换个关键词试试</EmptyDescription>
            </EmptyHeader>
          </Empty>
        </div>
      ) : null}

      {data && data.records.length > 0 ? (
        <>
          <div
            className={`grid gap-4 sm:grid-cols-2 lg:grid-cols-3 ${
              isFetching ? 'opacity-70' : ''
            }`}
          >
            {data.records.map((item) => (
              <PlayCard key={item.id} item={item} />
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
