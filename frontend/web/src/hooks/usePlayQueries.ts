import { keepPreviousData, useMutation, useQuery, useQueryClient } from '@tanstack/react-query'
import { playApi } from '@/api/play'
import { playOrderApi } from '@/api/order'

export const playKeys = {
  list: (keyword: string, page: number) => ['plays', 'list', keyword, page] as const,
  detail: (id: number) => ['plays', 'detail', id] as const,
}

export function usePlayList(keyword: string, page: number) {
  return useQuery({
    queryKey: playKeys.list(keyword, page),
    queryFn: () => playApi.list({ keyword: keyword || undefined, page, size: 12 }),
    staleTime: 60_000,
    placeholderData: keepPreviousData,
  })
}

export function usePlayDetail(id: number) {
  return useQuery({
    queryKey: playKeys.detail(id),
    queryFn: () => playApi.detail(id),
    enabled: Number.isFinite(id) && id > 0,
    staleTime: 60_000,
  })
}

export function useCreatePlayOrder() {
  const queryClient = useQueryClient()
  return useMutation({
    mutationFn: (body: { itemId: number; quantity: number }) => playOrderApi.create(body),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['plays'] })
      queryClient.invalidateQueries({ queryKey: ['orders'] })
    },
  })
}
