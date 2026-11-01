import { useMutation, useQuery, useQueryClient } from '@tanstack/react-query'
import { orderApi, type CreateOrderRequest } from '@/api/order'

export const orderKeys = {
  detail: (orderNo: string) => ['orders', 'detail', orderNo] as const,
}

export function useCreateOrder() {
  const queryClient = useQueryClient()
  return useMutation({
    mutationFn: (body: CreateOrderRequest) => orderApi.create(body),
    onSuccess: (order) => {
      queryClient.setQueryData(orderKeys.detail(order.orderNo), order)
      queryClient.invalidateQueries({ queryKey: ['cinemas'] })
      queryClient.invalidateQueries({ queryKey: ['screenings'] })
    },
    onError: () => {
      queryClient.invalidateQueries({ queryKey: ['screenings'] })
    },
  })
}

export function useOrderDetail(orderNo: string) {
  return useQuery({
    queryKey: orderKeys.detail(orderNo),
    queryFn: () => orderApi.detail(orderNo),
    enabled: /^\d+$/.test(orderNo),
    refetchInterval: (query) => (query.state.data?.status === 0 ? 30_000 : false),
  })
}
