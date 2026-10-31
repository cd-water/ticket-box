import { ThumbsUp } from 'lucide-react'
import type { Reply } from '@/api/movie'
import { Avatar, AvatarFallback, AvatarImage } from '@/components/ui/avatar'
import { useToggleLike } from '@/hooks/useMovieQueries'
import { fromNow } from '@/lib/date'

interface ReplyItemProps {
  movieId: number
  reply: Reply
  onReply: (reply: Reply) => void
}

export function ReplyItem({ movieId, reply, onReply }: ReplyItemProps) {
  const toggleLike = useToggleLike(movieId)

  return (
    <div className="flex gap-2 py-2">
      <Avatar size="sm">
        <AvatarImage src={reply.user.avatar} alt={reply.user.nickname} />
        <AvatarFallback>{reply.user.nickname.slice(0, 1)}</AvatarFallback>
      </Avatar>
      <div className="min-w-0 flex-1">
        <div className="flex flex-wrap items-center gap-1.5 text-xs">
          <span className="font-medium text-foreground">{reply.user.nickname}</span>
          {reply.replyTo && reply.parentId !== reply.rootId ? (
            <span className="text-muted-foreground">回复 @{reply.replyTo.nickname}</span>
          ) : null}
          <span className="text-muted-foreground">{fromNow(reply.createTime)}</span>
        </div>
        <p className="mt-1 whitespace-pre-wrap break-words text-sm leading-6 text-foreground/90">
          {reply.content}
        </p>
        <div className="mt-1 flex items-center gap-4 text-xs text-muted-foreground">
          <button
            type="button"
            aria-pressed={reply.liked}
            aria-label={reply.liked ? '取消点赞' : '点赞'}
            className={reply.liked ? 'flex items-center gap-1 text-primary' : 'flex items-center gap-1'}
            onClick={() => toggleLike.mutate(reply.id)}
          >
            <ThumbsUp className="size-3.5" />
            {reply.likeCount > 0 ? reply.likeCount : '点赞'}
          </button>
          <button type="button" onClick={() => onReply(reply)}>
            回复
          </button>
        </div>
      </div>
    </div>
  )
}
