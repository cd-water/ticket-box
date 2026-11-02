import { keepPreviousData, useMutation, useQuery, useQueryClient } from '@tanstack/react-query'
import { orderApi, type CreateOrderRequest } from '@/api/order'

export const orderKeys = {
  detail: (orderNo: string) => ['orders', 'movie', 'detail', orderNo] as const,
  list: (status: number, page: number) => ['orders', 'movie', 'list', status, page] as const,
}

export function useCreateOrder() {
  const queryClient = useQueryClient()
  return useMutation({
    mutationFn: (body: CreateOrderRequest) => orderApi.create(body),
    onSuccess: (order) => {
      queryClient.setQueryData(orderKeys.detail(order.orderNo), order)
      queryClient.invalidateQueries({ queryKey: ['cinemas'] })
      queryClient.invalidateQueries({ queryKey: ['screenings'] })
      queryClient.invalidateQueries({ queryKey: ['orders'] })
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

export function useMovieOrderList(status: number, page: number, enabled = true) {
  return useQuery({
    queryKey: orderKeys.list(status, page),
    queryFn: () => orderApi.list({ status, page, size: 10 }),
    enabled,
    staleTime: 10_000,
    placeholderData: keepPreviousData,
    refetchInterval: status === 0 ? 30_000 : false,
  })
}

export function usePayMovieOrder() {
  const queryClient = useQueryClient()
  return useMutation({
    mutationFn: (orderNo: string) => orderApi.pay(orderNo),
    onSuccess: (order) => {
      queryClient.setQueryData(orderKeys.detail(order.orderNo), order)
      queryClient.invalidateQueries({ queryKey: ['orders'] })
    },
    onError: () => {
      queryClient.invalidateQueries({ queryKey: ['orders'] })
    },
  })
}

export function useCancelMovieOrder() {
  const queryClient = useQueryClient()
  return useMutation({
    mutationFn: (orderNo: string) => orderApi.cancel(orderNo),
    onSuccess: (order) => {
      queryClient.setQueryData(orderKeys.detail(order.orderNo), order)
      queryClient.invalidateQueries({ queryKey: ['orders'] })
    },
    onError: () => {
      queryClient.invalidateQueries({ queryKey: ['orders'] })
    },
  })
}
