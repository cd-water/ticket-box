import {
  keepPreviousData,
  useInfiniteQuery,
  useMutation,
  useQuery,
  useQueryClient,
  type InfiniteData,
} from '@tanstack/react-query'
import {
  movieApi,
  type CursorResult,
  type MovieTab,
  type Reply,
  type Review,
  type ReviewSort,
} from '@/api/movie'

export const movieKeys = {
  list: (tab: MovieTab, keyword: string, page: number) =>
    ['movies', 'list', tab, keyword, page] as const,
  detail: (id: number) => ['movies', 'detail', id] as const,
  reviews: (id: number, sort: ReviewSort) => ['movies', id, 'reviews', sort] as const,
  replies: (rootId: number) => ['reviews', rootId, 'replies'] as const,
}

export function useMovieList(tab: MovieTab, keyword: string, page: number) {
  return useQuery({
    queryKey: movieKeys.list(tab, keyword, page),
    queryFn: () => movieApi.list({ tab, keyword: keyword || undefined, page }),
    staleTime: 60_000,
    placeholderData: keepPreviousData,
  })
}

export function useMovieDetail(id: number) {
  return useQuery({
    queryKey: movieKeys.detail(id),
    queryFn: () => movieApi.detail(id),
    staleTime: 5 * 60_000,
    enabled: Number.isFinite(id) && id > 0,
  })
}

export function useReviews(movieId: number, sort: ReviewSort) {
  return useInfiniteQuery({
    queryKey: movieKeys.reviews(movieId, sort),
    queryFn: ({ pageParam }) =>
      movieApi.reviews(movieId, { sort, cursor: pageParam || undefined }),
    initialPageParam: '',
    getNextPageParam: (last) => (last.hasMore ? last.nextCursor ?? undefined : undefined),
    staleTime: 30_000,
  })
}

export function useReplies(rootId: number, enabled: boolean) {
  return useInfiniteQuery({
    queryKey: movieKeys.replies(rootId),
    queryFn: ({ pageParam }) =>
      movieApi.replies(rootId, { cursor: pageParam || undefined }),
    initialPageParam: '',
    getNextPageParam: (last) => (last.hasMore ? last.nextCursor ?? undefined : undefined),
    enabled,
    staleTime: 30_000,
  })
}

export function useCreateReview(movieId: number) {
  const queryClient = useQueryClient()
  return useMutation({
    mutationFn: (content: string) => movieApi.createReview(movieId, content),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['movies', movieId, 'reviews'] })
      queryClient.invalidateQueries({ queryKey: movieKeys.detail(movieId) })
    },
  })
}

export function useReply(movieId: number) {
  const queryClient = useQueryClient()
  return useMutation({
    mutationFn: ({ targetId, content }: { targetId: number; content: string }) =>
      movieApi.reply(targetId, content),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['movies', movieId, 'reviews'] })
      queryClient.invalidateQueries({ queryKey: ['reviews'] })
      queryClient.invalidateQueries({ queryKey: movieKeys.detail(movieId) })
    },
  })
}

type Likeable = { id: number; liked: boolean; likeCount: number }

function toggleItem<T extends Likeable>(item: T): T {
  return {
    ...item,
    liked: !item.liked,
    likeCount: item.likeCount + (item.liked ? -1 : 1),
  }
}

function patchReviewPages(data: InfiniteData<CursorResult<Review>>, id: number) {
  return {
    ...data,
    pages: data.pages.map((page) => ({
      ...page,
      records: page.records.map((review) =>
        review.id === id
          ? toggleItem(review)
          : { ...review, replies: review.replies.map((r) => (r.id === id ? toggleItem(r) : r)) },
      ),
    })),
  }
}

function patchReplyPages(data: InfiniteData<CursorResult<Reply>>, id: number) {
  return {
    ...data,
    pages: data.pages.map((page) => ({
      ...page,
      records: page.records.map((reply) => (reply.id === id ? toggleItem(reply) : reply)),
    })),
  }
}

export function useToggleLike(movieId: number) {
  const queryClient = useQueryClient()
  return useMutation({
    mutationFn: (id: number) => movieApi.toggleLike(id),
    onMutate: async (id) => {
      const reviewKey = ['movies', movieId, 'reviews'] as const
      const replyKey = ['reviews'] as const
      await queryClient.cancelQueries({ queryKey: reviewKey })
      await queryClient.cancelQueries({ queryKey: replyKey })
      const previousReviews = queryClient.getQueriesData<InfiniteData<CursorResult<Review>>>({
        queryKey: reviewKey,
      })
      const previousReplies = queryClient.getQueriesData<InfiniteData<CursorResult<Reply>>>({
        queryKey: replyKey,
      })
      queryClient.setQueriesData<InfiniteData<CursorResult<Review>>>({ queryKey: reviewKey }, (data) =>
        data ? patchReviewPages(data, id) : data,
      )
      queryClient.setQueriesData<InfiniteData<CursorResult<Reply>>>({ queryKey: replyKey }, (data) =>
        data ? patchReplyPages(data, id) : data,
      )
      return { previousReviews, previousReplies }
    },
    onError: (_error, _id, context) => {
      context?.previousReviews.forEach(([key, data]) => queryClient.setQueryData(key, data))
      context?.previousReplies.forEach(([key, data]) => queryClient.setQueryData(key, data))
    },
    onSettled: () => {
      queryClient.invalidateQueries({ queryKey: ['movies', movieId, 'reviews'] })
      queryClient.invalidateQueries({ queryKey: ['reviews'] })
    },
  })
}
