import { useState } from 'react'
import type { Game, Platform } from '../data/games'

export const platformColors: Record<Platform, string> = {
  PS5:    '#003087',
  PS4:    '#003087',
  PC:     '#FF6B00',
  XSX:    '#107C10',
  Switch: '#E4000F',
}

export const platformLabels: Record<Platform, string> = {
  PS5:    'PS5',
  PS4:    'PS4',
  PC:     'PC',
  XSX:    'Xbox',
  Switch: 'Switch',
}

interface GameCardProps {
  game: Game
}

export default function GameCard({ game }: GameCardProps) {
  const [hovered, setHovered] = useState(false)

  return (
    <article
      className="relative rounded-xl overflow-hidden flex flex-col transition-all duration-300 cursor-default select-none"
      style={{
        background: hovered
          ? 'linear-gradient(160deg, #0F2760 0%, #1a0a3d 100%)'
          : 'linear-gradient(160deg, #0B1E4A 0%, #130536 100%)',
        border: hovered
          ? '1px solid rgba(192,132,252,0.4)'
          : '1px solid rgba(30,58,110,0.6)',
        transform: hovered ? 'translateY(-4px)' : 'translateY(0)',
        boxShadow: hovered
          ? '0 20px 40px rgba(0,0,0,0.4), 0 0 30px rgba(192,132,252,0.1)'
          : '0 4px 16px rgba(0,0,0,0.3)',
      }}
      onMouseEnter={() => setHovered(true)}
      onMouseLeave={() => setHovered(false)}
    >
      {/* Cover image */}
      <div className="relative overflow-hidden bg-[#071B42]" style={{ aspectRatio: '2/3', maxHeight: 300 }}>
        <img
          src={game.coverImage}
          alt={game.title}
          className="w-full h-full object-cover transition-transform duration-500"
          style={{ transform: hovered ? 'scale(1.06)' : 'scale(1)' }}
        />
        <div
          className="absolute inset-0"
          style={{ background: 'linear-gradient(to top, rgba(7,27,66,0.9) 0%, rgba(7,27,66,0.2) 50%, transparent 100%)' }}
        />

        {/* Genre badge */}
        <span
          className="absolute top-3 left-3 px-2 py-0.5 rounded text-xs font-semibold"
          style={{ background: 'rgba(62,7,120,0.85)', color: '#C084FC', backdropFilter: 'blur(4px)' }}
        >
          {game.genre}
        </span>

        {/* Stock badge */}
        <span
          className="absolute top-3 right-3 px-2 py-0.5 rounded text-xs font-semibold"
          style={{
            background: game.stock ? 'rgba(16,124,16,0.85)' : 'rgba(120,20,20,0.85)',
            color: game.stock ? '#86EFAC' : '#FCA5A5',
            backdropFilter: 'blur(4px)',
          }}
        >
          {game.stock ? 'En stock' : 'Agotado'}
        </span>

        {/* Rating overlay */}
        <div className="absolute bottom-3 left-3 flex items-center gap-1.5">
          <svg className="w-4 h-4" viewBox="0 0 24 24" fill="#C084FC">
            <path d="M11.48 3.499a.562.562 0 011.04 0l2.125 5.111a.563.563 0 00.475.345l5.518.442c.499.04.701.663.321.988l-4.204 3.602a.563.563 0 00-.182.557l1.285 5.385a.562.562 0 01-.84.61l-4.725-2.885a.563.563 0 00-.586 0L6.982 20.54a.562.562 0 01-.84-.61l1.285-5.386a.562.562 0 00-.182-.557l-4.204-3.602a.562.562 0 01.321-.988l5.518-.442a.563.563 0 00.475-.345L11.48 3.5z" />
          </svg>
          <span className="text-sm font-bold text-white" style={{ fontFamily: 'Oxanium, sans-serif' }}>
            {game.rating.toFixed(1)}
          </span>
          <span className="text-xs" style={{ color: '#7E9BBF' }}>
            ({(game.votes / 1000).toFixed(0)}k)
          </span>
        </div>
      </div>

      {/* Card body */}
      <div className="flex flex-col flex-1 p-4 gap-3">
        <div>
          <h3
            className="font-bold text-white leading-tight mb-0.5 line-clamp-2"
            style={{ fontFamily: 'Oxanium, sans-serif', fontSize: '1rem' }}
          >
            {game.title}
          </h3>
          <p className="text-xs" style={{ color: '#7E9BBF' }}>{game.developer}</p>
        </div>

        {/* Platforms */}
        <div className="flex gap-1 flex-wrap">
          {game.platforms.map(p => (
            <span
              key={p}
              className="px-2 py-0.5 rounded text-xs font-bold"
              style={{
                background: platformColors[p] + '22',
                color: platformColors[p],
                border: `1px solid ${platformColors[p]}44`,
              }}
            >
              {platformLabels[p]}
            </span>
          ))}
        </div>

        {/* Price + release */}
        <div className="flex items-end justify-between mt-auto pt-2" style={{ borderTop: '1px solid rgba(30,58,110,0.5)' }}>
          <div>
            <p className="text-xs mb-0.5" style={{ color: '#7E9BBF' }}>
              {new Date(game.releaseDate).toLocaleDateString('es-ES', { year: 'numeric', month: 'short' })}
            </p>
          </div>
          <div className="text-right">
            <p
              className="text-xl font-extrabold"
              style={{ fontFamily: 'Oxanium, sans-serif', color: '#C084FC' }}
            >
              ${game.price.toFixed(2)}
            </p>
            <p className="text-xs" style={{ color: '#7E9BBF' }}>USD</p>
          </div>
        </div>
      </div>
    </article>
  )
}
