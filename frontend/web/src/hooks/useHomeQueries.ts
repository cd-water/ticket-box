import { useQuery } from '@tanstack/react-query'
import { homeApi } from '@/api/home'

export function useHome() {
  return useQuery({
    queryKey: ['home'],
    queryFn: () => homeApi.get(),
    staleTime: 60_000,
  })
}
