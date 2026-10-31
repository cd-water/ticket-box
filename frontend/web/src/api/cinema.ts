import http from '@/api/request'
import type { PageResult } from '@/api/types'

export interface CinemaListItem {
  id: number
  name: string
  address: string
  phone: string
}

export interface CinemaMovie {
  id: number
  title: string
  poster: string
  releaseDate: string
  duration: number
}

export interface CinemaDetail {
  id: number
  name: string
  address: string
  phone: string
  movies: CinemaMovie[]
}

export interface Screening {
  id: number
  hallName: string
  startTime: string
  endTime: string
  price: number
  availableSeats: number
}

export interface Seat {
  seatId: number
  rowNum: number
  colNum: number
  status: number
}

export interface SeatMap {
  screeningId: number
  cinemaName: string
  movieTitle: string
  hallName: string
  startTime: string
  endTime: string
  price: number
  rowCount: number
  colCount: number
  seats: Seat[]
}

export const cinemaApi = {
  list: (params: { page: number; size?: number; name?: string; movieId?: number }) =>
    http.get<PageResult<CinemaListItem>>('/cinemas', { params }),
  detail: (id: number) => http.get<CinemaDetail>(`/cinemas/${id}`),
  screenings: (cinemaId: number, movieId: number) =>
    http.get<Screening[]>(`/cinemas/${cinemaId}/movies/${movieId}/screenings`),
  seatMap: (screeningId: number) =>
    http.get<SeatMap>(`/screenings/${screeningId}/seats`),
}
