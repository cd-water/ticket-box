import http from '@/api/request'

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
  createTime: string
  cinemaName: string
  movieTitle: string
  hallName: string
  startTime: string
  endTime: string
  seats: OrderSeat[]
}

export interface CreateOrderRequest {
  screeningId: number
  seatIds: number[]
}

export const orderApi = {
  create: (body: CreateOrderRequest) => http.post<OrderDetail>('/orders', body),
  detail: (orderNo: string) => http.get<OrderDetail>(`/orders/${orderNo}`),
}
