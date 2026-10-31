import http from '@/api/request'
import type { PageResult } from '@/api/types'

export type { PageResult } from '@/api/types'

export type MovieTab = 'showing' | 'upcoming'
export type ReviewSort = 'hot' | 'latest'

export interface CursorResult<T> {
  records: T[]
  nextCursor: string | null
  hasMore: boolean
}

export interface MovieListItem {
  id: number
  title: string
  poster: string
  releaseDate: string
}

export interface Genre {
  id: number
  name: string
}

export interface Crew {
  personId: number
  name: string
  avatar: string
  roleName: string | null
}

export interface MovieDetail {
  id: number
  title: string
  poster: string
  synopsis: string
  duration: number
  releaseDate: string
  tab: MovieTab
  genres: Genre[]
  directors: Crew[]
  actors: Crew[]
  reviewCount: number
}

export interface ReviewUser {
  id: number
  nickname: string
  avatar: string
}

export interface ReplyTo {
  id: number
  nickname: string
}

export interface Reply {
  id: number
  rootId: number
  parentId: number
  content: string
  createTime: string
  user: ReviewUser
  likeCount: number
  liked: boolean
  replyTo: ReplyTo | null
}

export interface Review {
  id: number
  content: string
  createTime: string
  user: ReviewUser
  likeCount: number
  liked: boolean
  replyCount: number
  replies: Reply[]
}

export interface LikeResult {
  liked: boolean
  likeCount: number
}

export const movieApi = {
  list: (params: { tab: MovieTab; keyword?: string; page: number; size?: number }) =>
    http.get<PageResult<MovieListItem>>('/movies', { params }),
  detail: (id: number) => http.get<MovieDetail>(`/movies/${id}`),
  reviews: (id: number, params: { sort: ReviewSort; cursor?: string; size?: number }) =>
    http.get<CursorResult<Review>>(`/movies/${id}/reviews`, { params }),
  replies: (rootId: number, params: { cursor?: string; size?: number }) =>
    http.get<CursorResult<Reply>>(`/reviews/${rootId}/replies`, { params }),
  createReview: (id: number, content: string) =>
    http.post<Review>(`/movies/${id}/reviews`, { content }),
  reply: (id: number, content: string) =>
    http.post<Reply>(`/reviews/${id}/reply`, { content }),
  toggleLike: (id: number) => http.post<LikeResult>(`/reviews/${id}/like`),
}
