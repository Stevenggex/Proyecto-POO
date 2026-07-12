import type { Game } from '../data/games'
import GameCard from './GameCard'

interface GameGridProps {
  games: Game[]
}

export default function GameGrid({ games }: GameGridProps) {
  if (games.length === 0) {
    return (
      <div className="flex flex-col items-center justify-center py-24 gap-4">
        <div
          className="w-16 h-16 rounded-full flex items-center justify-center"
          style={{ background: 'rgba(62,7,120,0.2)', border: '1px solid rgba(62,7,120,0.4)' }}
        >
          <svg className="w-8 h-8" style={{ color: '#7E9BBF' }} fill="none" stroke="currentColor" viewBox="0 0 24 24">
            <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={1.5} d="M21 21l-6-6m2-5a7 7 0 11-14 0 7 7 0 0114 0z" />
          </svg>
        </div>
        <p className="text-lg font-semibold" style={{ fontFamily: 'Oxanium, sans-serif', color: '#94A3B8' }}>
          Sin resultados
        </p>
        <p className="text-sm" style={{ color: '#7E9BBF' }}>
          Intenta con otros filtros o términos de búsqueda.
        </p>
      </div>
    )
  }

  return (
    <div
      className="grid gap-5"
      style={{
        gridTemplateColumns: 'repeat(auto-fill, minmax(200px, 1fr))',
      }}
    >
      {games.map(game => (
        <GameCard key={game.id} game={game} />
      ))}
    </div>
  )
}
