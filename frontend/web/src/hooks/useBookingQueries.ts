import { useMutation, useQuery, useQueryClient } from '@tanstack/react-query'
import { orderApi, type CreateOrderRequest } from '@/api/order'

export const orderKeys = {
  detail: (id: number) => ['orders', 'detail', id] as const,
}

export function useCreateOrder() {
  const queryClient = useQueryClient()
  return useMutation({
    mutationFn: (body: CreateOrderRequest) => orderApi.create(body),
    onSuccess: (order) => {
      queryClient.setQueryData(orderKeys.detail(order.id), order)
      queryClient.invalidateQueries({ queryKey: ['cinemas'] })
      queryClient.invalidateQueries({ queryKey: ['screenings'] })
    },
    onError: () => {
      queryClient.invalidateQueries({ queryKey: ['screenings'] })
    },
  })
}

export function useOrderDetail(id: number) {
  return useQuery({
    queryKey: orderKeys.detail(id),
    queryFn: () => orderApi.detail(id),
    enabled: Number.isFinite(id) && id > 0,
    refetchInterval: (query) => (query.state.data?.status === 0 ? 30_000 : false),
  })
}
