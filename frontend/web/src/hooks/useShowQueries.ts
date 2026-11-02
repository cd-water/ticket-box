import { keepPreviousData, useMutation, useQuery, useQueryClient } from '@tanstack/react-query'
import { showApi } from '@/api/show'
import { showOrderApi } from '@/api/order'

export const showKeys = {
  list: (keyword: string, page: number) => ['shows', 'list', keyword, page] as const,
  detail: (id: number) => ['shows', 'detail', id] as const,
}

export function useShowList(keyword: string, page: number) {
  return useQuery({
    queryKey: showKeys.list(keyword, page),
    queryFn: () => showApi.list({ keyword: keyword || undefined, page, size: 12 }),
    staleTime: 60_000,
    placeholderData: keepPreviousData,
  })
}

export function useShowDetail(id: number) {
  return useQuery({
    queryKey: showKeys.detail(id),
    queryFn: () => showApi.detail(id),
    enabled: Number.isFinite(id) && id > 0,
    staleTime: 60_000,
  })
}

export function useCreateShowOrder() {
  const queryClient = useQueryClient()
  return useMutation({
    mutationFn: (body: { showId: number; ticketTypeId: number; quantity: number }) =>
      showOrderApi.create(body),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['shows'] })
      queryClient.invalidateQueries({ queryKey: ['orders'] })
    },
  })
}
