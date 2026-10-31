import { useState } from 'react'
import { MessageCircle, ThumbsUp } from 'lucide-react'
import type { Review } from '@/api/movie'
import { Avatar, AvatarFallback, AvatarImage } from '@/components/ui/avatar'
import { ReviewComposer } from '@/components/movie/ReviewComposer'
import { ReplyItem } from '@/components/movie/ReplyItem'
import { useReplies, useToggleLike } from '@/hooks/useMovieQueries'
import { fromNow } from '@/lib/date'

export function ReviewItem({ movieId, review }: { movieId: number; review: Review }) {
  const [showReplies, setShowReplies] = useState(false)
  const [replyTarget, setReplyTarget] = useState<{ id: number; nickname: string } | null>(null)
  const repliesQuery = useReplies(review.id, showReplies)
  const toggleLike = useToggleLike(movieId)

  const replies = showReplies
    ? repliesQuery.data?.pages.flatMap((page) => page.records) ?? review.replies
    : review.replies

  return (
    <article className="flex gap-3 py-4">
      <Avatar>
        <AvatarImage src={review.user.avatar} alt={review.user.nickname} />
        <AvatarFallback>{review.user.nickname.slice(0, 1)}</AvatarFallback>
      </Avatar>

      <div className="min-w-0 flex-1">
        <div className="flex items-center gap-2 text-sm">
          <span className="font-medium text-foreground">{review.user.nickname}</span>
          <span className="text-xs text-muted-foreground">{fromNow(review.createTime)}</span>
        </div>

        <p className="mt-1 whitespace-pre-wrap break-words text-sm leading-6 text-foreground/90">
          {review.content}
        </p>

        <div className="mt-2 flex items-center gap-4 text-xs text-muted-foreground">
          <button
            type="button"
            aria-pressed={review.liked}
            aria-label={review.liked ? '取消点赞' : '点赞'}
            className={review.liked ? 'flex items-center gap-1 text-primary' : 'flex items-center gap-1'}
            onClick={() => toggleLike.mutate(review.id)}
          >
            <ThumbsUp className="size-4" />
            {review.likeCount > 0 ? review.likeCount : '点赞'}
          </button>
          <button
            type="button"
            className="flex items-center gap-1"
            onClick={() => setReplyTarget({ id: review.id, nickname: review.user.nickname })}
          >
            <MessageCircle className="size-4" />
            回复
          </button>
        </div>

        {replies.length > 0 ? (
          <div className="mt-3 space-y-1">
            {replies.map((reply) => (
              <ReplyItem
                key={reply.id}
                movieId={movieId}
                reply={reply}
                onReply={(target) =>
                  setReplyTarget({ id: target.id, nickname: target.user.nickname })
                }
              />
            ))}

            {!showReplies && review.replyCount > 2 ? (
              <button
                type="button"
                className="text-xs text-primary hover:underline"
                onClick={() => setShowReplies(true)}
              >
                共 {review.replyCount} 条回复，查看更多
              </button>
            ) : null}

            {showReplies && repliesQuery.hasNextPage ? (
              <button
                type="button"
                className="text-xs text-primary hover:underline"
                disabled={repliesQuery.isFetchingNextPage}
                onClick={() => void repliesQuery.fetchNextPage()}
              >
                {repliesQuery.isFetchingNextPage ? '加载中…' : '加载更多回复'}
              </button>
            ) : null}
          </div>
        ) : null}

        {replyTarget ? (
          <div className="mt-3">
            <ReviewComposer
              movieId={movieId}
              targetId={replyTarget.id}
              compact
              placeholder={`回复 @${replyTarget.nickname}`}
              onDone={() => setReplyTarget(null)}
            />
          </div>
        ) : null}
      </div>
    </article>
  )
}
