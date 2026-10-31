import { Film } from 'lucide-react'
import { Link } from 'react-router'
import type { MovieListItem } from '@/api/movie'
import { formatDateTime } from '@/lib/date'

export function MovieCard({ movie }: { movie: MovieListItem }) {
  return (
    <Link to={`/movie/${movie.id}`} className="group block">
      <div className="aspect-[2/3] overflow-hidden rounded-lg bg-muted">
        {movie.poster ? (
          <img
            src={movie.poster}
            alt={movie.title}
            loading="lazy"
            className="size-full object-cover transition-transform duration-300 group-hover:scale-105"
          />
        ) : (
          <div className="flex size-full items-center justify-center text-muted-foreground">
            <Film className="size-8" />
          </div>
        )}
      </div>
      <h3 className="mt-2 truncate text-sm font-medium text-foreground">{movie.title}</h3>
      <p className="text-xs text-muted-foreground">{formatDateTime(movie.releaseDate)}</p>
    </Link>
  )
}
