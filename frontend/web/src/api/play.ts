import http from '@/api/request'
import type { PageResult } from '@/api/types'

export interface PlayListItem {
  id: number
  title: string
  cover: string
  address: string
  price: number
  stock: number
}

export interface PlayDetail extends PlayListItem {
  description: string
}

export const playApi = {
  list: (params: { keyword?: string; page: number; size?: number }) =>
    http.get<PageResult<PlayListItem>>('/plays', { params }),
  detail: (id: number) => http.get<PlayDetail>(`/plays/${id}`),
}
