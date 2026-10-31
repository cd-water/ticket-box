import { useState } from 'react'
import { Button } from '@/components/ui/button'
import { Textarea } from '@/components/ui/textarea'
import { useCreateReview, useReply } from '@/hooks/useMovieQueries'

interface ReviewComposerProps {
  movieId: number
  targetId?: number
  placeholder?: string
  compact?: boolean
  onDone?: () => void
}

export function ReviewComposer({
  movieId,
  targetId,
  placeholder = '说说你对这部电影的看法…',
  compact = false,
  onDone,
}: ReviewComposerProps) {
  const [content, setContent] = useState('')
  const createReview = useCreateReview(movieId)
  const reply = useReply(movieId)
  const pending = createReview.isPending || reply.isPending

  function submit() {
    const value = content.trim()
    if (!value) return
    const onSuccess = () => {
      setContent('')
      onDone?.()
    }
    if (targetId) {
      reply.mutate({ targetId, content: value }, { onSuccess })
    } else {
      createReview.mutate(value, { onSuccess })
    }
  }

  return (
    <div className="space-y-2">
      <Textarea
        value={content}
        onChange={(event) => setContent(event.target.value)}
        placeholder={placeholder}
        maxLength={1000}
        aria-label={placeholder}
        className={compact ? 'min-h-20 bg-card' : 'min-h-28 bg-card'}
      />
      <div className="flex items-center justify-between">
        <span className="text-xs text-muted-foreground">{content.length}/1000</span>
        <div className="flex gap-2">
          {compact ? (
            <Button variant="ghost" size="sm" onClick={() => onDone?.()}>
              取消
            </Button>
          ) : null}
          <Button size="sm" disabled={!content.trim() || pending} onClick={submit}>
            {pending ? '发布中…' : '发布'}
          </Button>
        </div>
      </div>
    </div>
  )
}
