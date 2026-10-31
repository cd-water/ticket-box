import { keepPreviousData, useQuery } from '@tanstack/react-query'
import { cinemaApi } from '@/api/cinema'

export const cinemaKeys = {
  list: (name: string, page: number, movieId?: number) =>
    ['cinemas', 'list', name, page, movieId ?? 0] as const,
  detail: (id: number) => ['cinemas', 'detail', id] as const,
  screenings: (cinemaId: number, movieId: number) =>
    ['cinemas', cinemaId, 'movies', movieId, 'screenings'] as const,
  seatMap: (screeningId: number) => ['screenings', screeningId, 'seats'] as const,
}

export function useCinemaList(name: string, page: number, movieId?: number) {
  return useQuery({
    queryKey: cinemaKeys.list(name, page, movieId),
    queryFn: () => cinemaApi.list({
      page,
      size: 10,
      name: name || undefined,
      movieId: movieId || undefined,
    }),
    staleTime: 60_000,
    placeholderData: keepPreviousData,
  })
}

export function useCinemaDetail(id: number) {
  return useQuery({
    queryKey: cinemaKeys.detail(id),
    queryFn: () => cinemaApi.detail(id),
    enabled: Number.isFinite(id) && id > 0,
    staleTime: 60_000,
  })
}

export function useScreenings(cinemaId: number, movieId: number) {
  return useQuery({
    queryKey: cinemaKeys.screenings(cinemaId, movieId),
    queryFn: () => cinemaApi.screenings(cinemaId, movieId),
    enabled: cinemaId > 0 && movieId > 0,
    staleTime: 30_000,
  })
}

export function useSeatMap(screeningId: number) {
  return useQuery({
    queryKey: cinemaKeys.seatMap(screeningId),
    queryFn: () => cinemaApi.seatMap(screeningId),
    enabled: screeningId > 0,
    staleTime: 0,
  })
}
