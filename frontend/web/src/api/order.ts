import http from '@/api/request'
import type { PageResult } from '@/api/types'

export interface OrderSeat {
  seatId: number
  rowNum: number
  colNum: number
  price: number
}

export interface OrderDetail {
  orderNo: string
  status: number
  totalAmount: number
  expireTime: string
  payTime: string | null
  createTime: string
  cinemaName: string
  movieTitle: string
  hallName: string
  startTime: string
  endTime: string
  seats: OrderSeat[]
}

export type MovieOrderListItem = Omit<OrderDetail, 'seats'>

export interface CreateOrderRequest {
  screeningId: number
  seatIds: number[]
}

export const orderApi = {
  create: (body: CreateOrderRequest) => http.post<OrderDetail>('/movie-orders', body),
  detail: (orderNo: string) => http.get<OrderDetail>(`/movie-orders/${orderNo}`),
  list: (params: { status: number; page: number; size?: number }) =>
    http.get<PageResult<MovieOrderListItem>>('/movie-orders', { params }),
  pay: (orderNo: string) => http.post<OrderDetail>(`/movie-orders/${orderNo}/pay`),
  cancel: (orderNo: string) => http.post<OrderDetail>(`/movie-orders/${orderNo}/cancel`),
}

export interface ShowOrderSeat {
  seatId: number
  zone: string
  seatNo: number
  price: number
}

export interface ShowOrderDetail {
  orderNo: string
  status: number
  totalAmount: number
  expireTime: string
  payTime: string | null
  createTime: string
  showTitle: string
  venueName: string
  venueAddress: string
  startTime: string
  ticketTypeName: string
  zone: string
  unitPrice: number
  quantity: number
  seats: ShowOrderSeat[]
}

export type ShowOrderListItem = Omit<ShowOrderDetail, 'seats'>

export const showOrderApi = {
  create: (body: { showId: number; ticketTypeId: number; quantity: number }) =>
    http.post<ShowOrderDetail>('/show-orders', body),
  detail: (orderNo: string) => http.get<ShowOrderDetail>(`/show-orders/${orderNo}`),
  list: (params: { status: number; page: number; size?: number }) =>
    http.get<PageResult<ShowOrderListItem>>('/show-orders', { params }),
  pay: (orderNo: string) => http.post<ShowOrderDetail>(`/show-orders/${orderNo}/pay`),
  cancel: (orderNo: string) => http.post<ShowOrderDetail>(`/show-orders/${orderNo}/cancel`),
}

export interface PlayOrderDetail {
  orderNo: string
  status: number
  totalAmount: number
  expireTime: string
  payTime: string | null
  createTime: string
  itemTitle: string
  itemAddress: string
  unitPrice: number
  quantity: number
}

export const playOrderApi = {
  create: (body: { itemId: number; quantity: number }) =>
    http.post<PlayOrderDetail>('/play-orders', body),
  detail: (orderNo: string) => http.get<PlayOrderDetail>(`/play-orders/${orderNo}`),
  list: (params: { status: number; page: number; size?: number }) =>
    http.get<PageResult<PlayOrderDetail>>('/play-orders', { params }),
  pay: (orderNo: string) => http.post<PlayOrderDetail>(`/play-orders/${orderNo}/pay`),
  cancel: (orderNo: string) => http.post<PlayOrderDetail>(`/play-orders/${orderNo}/cancel`),
}
