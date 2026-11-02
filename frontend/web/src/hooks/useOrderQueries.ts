import { useMutation, useQuery, useQueryClient } from '@tanstack/react-query'
import { playOrderApi, showOrderApi } from '@/api/order'

export const showOrderKeys = {
  detail: (orderNo: string) => ['orders', 'show', orderNo] as const,
  list: (status: number, page: number) => ['orders', 'show', 'list', status, page] as const,
}

export function useShowOrderDetail(orderNo: string) {
  return useQuery({
    queryKey: showOrderKeys.detail(orderNo),
    queryFn: () => showOrderApi.detail(orderNo),
    enabled: /^\d+$/.test(orderNo),
    refetchInterval: (query) => (query.state.data?.status === 0 ? 30_000 : false),
  })
}

export function useShowOrderList(status: number, page: number, enabled = true) {
  return useQuery({
    queryKey: showOrderKeys.list(status, page),
    queryFn: () => showOrderApi.list({ status, page, size: 10 }),
    enabled,
    staleTime: 10_000,
    refetchInterval: status === 0 ? 30_000 : false,
  })
}

export function usePayShowOrder() {
  const queryClient = useQueryClient()
  return useMutation({
    mutationFn: (orderNo: string) => showOrderApi.pay(orderNo),
    onSuccess: (order) => {
      queryClient.setQueryData(showOrderKeys.detail(order.orderNo), order)
      queryClient.invalidateQueries({ queryKey: ['orders'] })
      queryClient.invalidateQueries({ queryKey: ['shows'] })
    },
    onError: () => {
      queryClient.invalidateQueries({ queryKey: ['orders'] })
    },
  })
}

export function useCancelShowOrder() {
  const queryClient = useQueryClient()
  return useMutation({
    mutationFn: (orderNo: string) => showOrderApi.cancel(orderNo),
    onSuccess: (order) => {
      queryClient.setQueryData(showOrderKeys.detail(order.orderNo), order)
      queryClient.invalidateQueries({ queryKey: ['orders'] })
    },
    onError: () => {
      queryClient.invalidateQueries({ queryKey: ['orders'] })
    },
  })
}

export const playOrderKeys = {
  detail: (orderNo: string) => ['orders', 'play', orderNo] as const,
  list: (status: number, page: number) => ['orders', 'play', 'list', status, page] as const,
}

export function usePlayOrderDetail(orderNo: string) {
  return useQuery({
    queryKey: playOrderKeys.detail(orderNo),
    queryFn: () => playOrderApi.detail(orderNo),
    enabled: /^\d+$/.test(orderNo),
    refetchInterval: (query) => (query.state.data?.status === 0 ? 30_000 : false),
  })
}

export function usePlayOrderList(status: number, page: number, enabled = true) {
  return useQuery({
    queryKey: playOrderKeys.list(status, page),
    queryFn: () => playOrderApi.list({ status, page, size: 10 }),
    enabled,
    staleTime: 10_000,
    refetchInterval: status === 0 ? 30_000 : false,
  })
}

export function usePayPlayOrder() {
  const queryClient = useQueryClient()
  return useMutation({
    mutationFn: (orderNo: string) => playOrderApi.pay(orderNo),
    onSuccess: (order) => {
      queryClient.setQueryData(playOrderKeys.detail(order.orderNo), order)
      queryClient.invalidateQueries({ queryKey: ['orders'] })
      queryClient.invalidateQueries({ queryKey: ['plays'] })
    },
    onError: () => {
      queryClient.invalidateQueries({ queryKey: ['orders'] })
    },
  })
}

export function useCancelPlayOrder() {
  const queryClient = useQueryClient()
  return useMutation({
    mutationFn: (orderNo: string) => playOrderApi.cancel(orderNo),
    onSuccess: (order) => {
      queryClient.setQueryData(playOrderKeys.detail(order.orderNo), order)
      queryClient.invalidateQueries({ queryKey: ['orders'] })
      queryClient.invalidateQueries({ queryKey: ['plays'] })
    },
    onError: () => {
      queryClient.invalidateQueries({ queryKey: ['orders'] })
    },
  })
}
