import http from '@/api/request'
import type { PageResult } from '@/api/types'

export interface ShowListItem {
  id: number
  title: string
  poster: string
  venueName: string
  venueAddress: string
  startTime: string
  minPrice: number
}

export interface TicketType {
  id: number
  name: string
  zone: string
  price: number
  availableSeats: number
}

export interface ShowDetail {
  id: number
  title: string
  poster: string
  description: string
  duration: number
  venueName: string
  venueAddress: string
  startTime: string
  ticketTypes: TicketType[]
}

export const showApi = {
  list: (params: { keyword?: string; page: number; size?: number }) =>
    http.get<PageResult<ShowListItem>>('/shows', { params }),
  detail: (id: number) => http.get<ShowDetail>(`/shows/${id}`),
}
