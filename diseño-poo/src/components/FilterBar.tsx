import type { Genre } from '../data/games'
import { genres as allGenres } from '../data/games'

interface FilterBarProps {
  selectedGenre: Genre | 'Todos'
  sortBy: 'rating' | 'price-asc' | 'price-desc' | 'title'
  onGenreChange: (g: Genre | 'Todos') => void
  onSortChange: (s: 'rating' | 'price-asc' | 'price-desc' | 'title') => void
  totalResults: number
  availableGenres: Genre[]
}

const sortOptions = [
  { value: 'rating', label: 'Mejor valorados' },
  { value: 'price-asc', label: 'Precio: menor a mayor' },
  { value: 'price-desc', label: 'Precio: mayor a menor' },
  { value: 'title', label: 'Título A-Z' },
] as const

export default function FilterBar({
  selectedGenre, sortBy,
  onGenreChange, onSortChange,
  totalResults, availableGenres,
}: FilterBarProps) {
  return (
    <div className="py-6 space-y-4">
      {/* Header row */}
      <div className="flex items-center justify-between flex-wrap gap-3">
        <div>
          <h2
            className="text-2xl font-bold text-white"
            style={{ fontFamily: 'Oxanium, sans-serif' }}
          >
            Catálogo de Juegos
          </h2>
          <p className="text-sm mt-0.5" style={{ color: '#7E9BBF' }}>
            {totalResults} {totalResults === 1 ? 'juego encontrado' : 'juegos encontrados'} · Solo lectura
          </p>
        </div>

        {/* Sort */}
        <div className="flex items-center gap-2">
          <span className="text-sm" style={{ color: '#7E9BBF' }}>Ordenar:</span>
          <select
            value={sortBy}
            onChange={e => onSortChange(e.target.value as typeof sortBy)}
            className="px-3 py-2 rounded-lg text-sm text-white outline-none cursor-pointer"
            style={{
              background: '#0B1E4A',
              border: '1px solid rgba(62,7,120,0.5)',
              color: '#E2E8F0',
            }}
          >
            {sortOptions.map(o => (
              <option key={o.value} value={o.value}>{o.label}</option>
            ))}
          </select>
        </div>
      </div>

      {/* Genre chips */}
      <div className="flex flex-wrap gap-2 items-center">
        <span className="text-xs font-medium mr-1" style={{ color: '#7E9BBF' }}>Género:</span>
        {(['Todos', ...allGenres.filter(g => availableGenres.includes(g))] as (Genre | 'Todos')[]).map(g => {
          const active = selectedGenre === g
          return (
            <button
              key={g}
              onClick={() => onGenreChange(g as Genre | 'Todos')}
              className="px-3 py-1.5 rounded-full text-xs font-medium transition-all duration-150"
              style={{
                background: active ? 'linear-gradient(135deg, #3E0778, #040A6B)' : 'rgba(11,30,74,0.8)',
                color: active ? '#E879F9' : '#94A3B8',
                border: active ? '1px solid rgba(232,121,249,0.4)' : '1px solid rgba(30,58,110,0.6)',
                boxShadow: active ? '0 0 12px rgba(192,132,252,0.2)' : 'none',
              }}
            >
              {g}
            </button>
          )
        })}
      </div>

      {/* Divider */}
      <div style={{ height: 1, background: 'linear-gradient(90deg, rgba(62,7,120,0.5), transparent)' }} />
    </div>
  )
}
