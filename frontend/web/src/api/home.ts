import http from '@/api/request'

export interface HomeBanner {
  id: number
  imageUrl: string
  linkUrl: string
}

export interface HomeMovie {
  id: number
  title: string
  poster: string
  releaseDate: string
}

export interface HomeShow {
  id: number
  title: string
  poster: string
  venueName: string
  venueAddress: string
  startTime: string
  minPrice: number
}

export interface HomePlay {
  id: number
  title: string
  cover: string
  address: string
  price: number
  stock: number
}

export interface HomeData {
  banners: HomeBanner[]
  movies: HomeMovie[]
  shows: HomeShow[]
  plays: HomePlay[]
}

export const homeApi = {
  get: () => http.get<HomeData>('/home'),
}
