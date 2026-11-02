import { useState } from 'react'
import { useSearchParams } from 'react-router'
import {
  OrderListCard,
  type OrderCardData,
  type OrderType,
} from '@/components/order/OrderListCard'
import { Button } from '@/components/ui/button'
import { ConfirmDialog } from '@/components/ui/confirm-dialog'
import {
  Empty,
  EmptyDescription,
  EmptyHeader,
  EmptyTitle,
} from '@/components/ui/empty'
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
import { Tabs, TabsList, TabsTrigger } from '@/components/ui/tabs'
import { toast } from '@/components/ui/toast'
import { getPageItems } from '@/lib/pagination'
import {
  useCancelMovieOrder,
  useMovieOrderList,
  usePayMovieOrder,
} from '@/hooks/useBookingQueries'
import {
  useCancelPlayOrder,
  useCancelShowOrder,
  usePayPlayOrder,
  usePayShowOrder,
  usePlayOrderList,
  useShowOrderList,
} from '@/hooks/useOrderQueries'

const PAGE_SIZE = 10

const MODULES: { value: OrderType; label: string }[] = [
  { value: 'movie', label: '电影订单' },
  { value: 'show', label: '演出订单' },
  { value: 'play', label: '玩乐订单' },
]

const STATUSES = [
  { value: '0', label: '待支付' },
  { value: '1', label: '已支付' },
  { value: '2', label: '已取消' },
]

export default function OrdersPage() {
  const [params, setParams] = useSearchParams()
  const type: OrderType =
    params.get('type') === 'show'
      ? 'show'
      : params.get('type') === 'play'
        ? 'play'
        : 'movie'
  const status = Math.min(2, Math.max(0, Number(params.get('status') ?? 0) || 0))
  const page = Math.max(1, Number(params.get('page') ?? 1) || 1)

  const movieQuery = useMovieOrderList(status, page, type === 'movie')
  const showQuery = useShowOrderList(status, page, type === 'show')
  const playQuery = usePlayOrderList(status, page, type === 'play')
  const { data, isPending, isError, refetch, isFetching } =
    type === 'movie' ? movieQuery : type === 'show' ? showQuery : playQuery

  const payMovie = usePayMovieOrder()
  const payShow = usePayShowOrder()
  const payPlay = usePayPlayOrder()
  const cancelMovie = useCancelMovieOrder()
  const cancelShow = useCancelShowOrder()
  const cancelPlay = useCancelPlayOrder()
  const busy =
    payMovie.isPending ||
    payShow.isPending ||
    payPlay.isPending ||
    cancelMovie.isPending ||
    cancelShow.isPending ||
    cancelPlay.isPending

  const [cancelTarget, setCancelTarget] = useState<string | null>(null)
  const totalPages = data ? Math.max(1, Math.ceil(data.total / PAGE_SIZE)) : 1
  const records = (data?.records ?? []) as unknown as OrderCardData[]

  function update(next: Record<string, string>) {
    const search = new URLSearchParams(params)
    Object.entries(next).forEach(([key, value]) => search.set(key, value))
    setParams(search)
  }

  function handlePay(orderNo: string) {
    const onSuccess = () => toast.add({ title: '支付成功', type: 'success' })
    const onError = () => toast.add({ title: '支付失败，请稍后重试', type: 'error' })
    if (type === 'movie') payMovie.mutate(orderNo, { onSuccess, onError })
    else if (type === 'show') payShow.mutate(orderNo, { onSuccess, onError })
    else payPlay.mutate(orderNo, { onSuccess, onError })
  }

  function handleCancel() {
    if (!cancelTarget) return
    const orderNo = cancelTarget
    const onSuccess = () => {
      setCancelTarget(null)
      toast.add({ title: '订单已取消', type: 'info' })
    }
    const onError = () => setCancelTarget(null)
    if (type === 'movie') cancelMovie.mutate(orderNo, { onSuccess, onError })
    else if (type === 'show') cancelShow.mutate(orderNo, { onSuccess, onError })
    else cancelPlay.mutate(orderNo, { onSuccess, onError })
  }

  function changePage(nextPage: number) {
    if (nextPage < 1 || nextPage > totalPages) return
    update({ page: String(nextPage) })
    window.scrollTo({ top: 0, behavior: 'smooth' })
  }

  return (
    <div className="space-y-6">
      <h1 className="text-2xl font-semibold text-foreground">我的订单</h1>

      <Tabs
        value={type}
        onValueChange={(value) =>
          update({ type: String(value), status: '0', page: '1' })
        }
      >
        <TabsList className="w-full max-w-md">
          {MODULES.map((module) => (
            <TabsTrigger key={module.value} value={module.value}>
              {module.label}
            </TabsTrigger>
          ))}
        </TabsList>
      </Tabs>

      <Tabs
        value={String(status)}
        onValueChange={(value) => update({ status: String(value), page: '1' })}
      >
        <TabsList>
          {STATUSES.map((item) => (
            <TabsTrigger key={item.value} value={item.value}>
              {item.label}
            </TabsTrigger>
          ))}
        </TabsList>
      </Tabs>

      {isPending ? (
        <div className="space-y-3">
          {Array.from({ length: 3 }).map((_, index) => (
            <Skeleton key={index} className="h-32 w-full rounded-xl" />
          ))}
        </div>
      ) : null}

      {isError ? (
        <div className="flex min-h-[40vh] items-center justify-center">
          <div className="text-center">
            <p className="text-muted-foreground">订单加载失败</p>
            <Button className="mt-4" variant="outline" onClick={() => void refetch()}>
              重试
            </Button>
          </div>
        </div>
      ) : null}

      {data && records.length === 0 ? (
        <div className="flex min-h-[40vh] items-center justify-center">
          <Empty className="p-0">
            <EmptyHeader>
              <EmptyTitle className="text-lg">暂无订单</EmptyTitle>
              <EmptyDescription>切换其他状态或去逛逛</EmptyDescription>
            </EmptyHeader>
          </Empty>
        </div>
      ) : null}

      {records.length > 0 ? (
        <>
          <div className={`space-y-3 ${isFetching ? 'opacity-70' : ''}`}>
            {records.map((order) => (
              <OrderListCard
                key={order.orderNo}
                type={type}
                order={order}
                busy={busy}
                onPay={() => handlePay(order.orderNo)}
                onCancel={() => setCancelTarget(order.orderNo)}
              />
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

      <ConfirmDialog
        open={cancelTarget !== null}
        onOpenChange={(open) => {
          if (!open) setCancelTarget(null)
        }}
        title="取消订单"
        description="取消后订单不可恢复，座位/库存将被释放。确定取消吗？"
        confirmText="确认取消"
        onConfirm={handleCancel}
      />
    </div>
  )
}
