import { useState } from 'react'
import { ArrowLeft, MapPin } from 'lucide-react'
import { Link, useNavigate, useParams } from 'react-router'
import { QuantityStepper } from '@/components/show/QuantityStepper'
import { Button } from '@/components/ui/button'
import {
  Empty,
  EmptyDescription,
  EmptyHeader,
  EmptyTitle,
} from '@/components/ui/empty'
import { Skeleton } from '@/components/ui/skeleton'
import { toast } from '@/components/ui/toast'
import { useCreatePlayOrder, usePlayDetail } from '@/hooks/usePlayQueries'

export default function PlayDetailPage() {
  const params = useParams()
  const navigate = useNavigate()
  const itemId = Number(params.id)
  const { data, isPending, isError } = usePlayDetail(itemId)
  const createOrder = useCreatePlayOrder()
  const [quantity, setQuantity] = useState(1)

  if (Number.isFinite(itemId) && itemId > 0 && isPending) {
    return (
      <div className="grid gap-8 md:grid-cols-[420px_1fr]">
        <Skeleton className="aspect-[16/10] w-full rounded-xl" />
        <div className="space-y-4">
          <Skeleton className="h-9 w-2/3" />
          <Skeleton className="h-4 w-1/2" />
          <Skeleton className="h-24 w-full" />
        </div>
      </div>
    )
  }

  if (!Number.isFinite(itemId) || itemId <= 0 || isError || !data) {
    return (
      <div className="flex min-h-[50vh] items-center justify-center">
        <Empty className="p-0">
          <EmptyHeader>
            <EmptyTitle className="text-lg">项目不存在或已下架</EmptyTitle>
            <EmptyDescription>返回玩乐页看看其他项目</EmptyDescription>
          </EmptyHeader>
          <Button variant="outline" nativeButton={false} render={<Link to="/play" />}>
            <ArrowLeft />
            返回玩乐
          </Button>
        </Empty>
      </div>
    )
  }

  const detail = data
  const max = Math.min(10, detail.stock)
  const currentQuantity = Math.max(1, Math.min(quantity, max || 1))

  function buy() {
    if (max === 0) return
    createOrder.mutate(
      { itemId: detail.id, quantity: currentQuantity },
      {
        onSuccess: (order) => navigate(`/order/play/${order.orderNo}`),
        onError: () => toast.add({ title: '下单失败，请稍后重试', type: 'error' }),
      },
    )
  }

  return (
    <div className="space-y-8">
      <Button variant="ghost" size="sm" nativeButton={false} render={<Link to="/play" />}>
        <ArrowLeft />
        返回玩乐
      </Button>

      <section className="grid gap-8 md:grid-cols-[420px_1fr]">
        <img
          src={detail.cover}
          alt={detail.title}
          className="aspect-[16/10] w-full rounded-xl object-cover"
        />
        <div className="space-y-4">
          <h1 className="text-3xl font-semibold text-foreground">{detail.title}</h1>
          <p className="flex items-center gap-2 text-sm text-muted-foreground">
            <MapPin className="size-4" />
            {detail.address}
          </p>
          <p className="text-2xl font-semibold text-primary">
            ¥{detail.price.toFixed(2)}
            <span className="ml-2 text-sm font-normal text-muted-foreground">
              剩余 {detail.stock} 张
            </span>
          </p>
          <p className="whitespace-pre-wrap text-sm leading-7 text-foreground/90">
            {detail.description}
          </p>
        </div>
      </section>

      <section className="flex flex-wrap items-center gap-6 rounded-xl border border-border bg-card p-5">
        <div className="space-y-1">
          <p className="text-sm text-muted-foreground">购买数量</p>
          <QuantityStepper value={currentQuantity} max={max} onChange={setQuantity} />
        </div>
        <div className="space-y-1">
          <p className="text-sm text-muted-foreground">合计</p>
          <p className="text-2xl font-semibold text-primary">
            ¥{(detail.price * currentQuantity).toFixed(2)}
          </p>
        </div>
        <Button
          size="lg"
          className="ml-auto h-10 px-8"
          disabled={max === 0 || createOrder.isPending}
          onClick={buy}
        >
          {createOrder.isPending ? '提交中…' : '立即购买'}
        </Button>
      </section>
    </div>
  )
}
