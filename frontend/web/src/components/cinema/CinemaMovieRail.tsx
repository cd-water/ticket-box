import type { CinemaMovie } from '@/api/cinema'

interface CinemaMovieRailProps {
  movies: CinemaMovie[]
  selectedId: number
  onSelect: (id: number) => void
}

export function CinemaMovieRail({ movies, selectedId, onSelect }: CinemaMovieRailProps) {
  return (
    <div className="flex gap-3 overflow-x-auto pb-2 [scrollbar-width:thin]">
      {movies.map((movie) => (
        <button
          key={movie.id}
          type="button"
          aria-pressed={movie.id === selectedId}
          onClick={() => onSelect(movie.id)}
          className={`w-28 shrink-0 rounded-lg border p-1 text-left transition-colors ${
            movie.id === selectedId
              ? 'border-primary bg-secondary'
              : 'border-border bg-card hover:border-primary/50'
          }`}
        >
          <div className="aspect-[2/3] overflow-hidden rounded-md bg-muted">
            {movie.poster ? (
              <img src={movie.poster} alt={movie.title} loading="lazy" className="size-full object-cover" />
            ) : null}
          </div>
          <p className="mt-1.5 truncate text-xs font-medium text-foreground">{movie.title}</p>
        </button>
      ))}
    </div>
  )
}
