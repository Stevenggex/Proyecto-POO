import type { Game } from '../data/games'

interface HeroBannerProps {
  game: Game
}

export default function HeroBanner({ game }: HeroBannerProps) {
  return (
    <section className="relative w-full overflow-hidden" style={{ minHeight: '480px' }}>
      {/* Background image */}
      <div className="absolute inset-0">
        <img
          src={`https://images.unsplash.com/photo-1616588589676-62b3bd4ff6d2?w=1600&h=600&fit=crop&auto=format`}
          alt="Gaming setup"
          className="w-full h-full object-cover"
        />
        <div
          className="absolute inset-0"
          style={{
            background: 'linear-gradient(90deg, rgba(7,27,66,0.98) 0%, rgba(4,10,107,0.85) 50%, rgba(62,7,120,0.4) 100%)',
          }}
        />
        <div
          className="absolute inset-0"
          style={{
            background: 'linear-gradient(to top, #071B42 0%, transparent 60%)',
          }}
        />
      </div>

      {/* Content */}
      <div className="relative max-w-[1400px] mx-auto px-4 sm:px-6 lg:px-8 py-16 flex items-center gap-12">
        {/* Text side */}
        <div className="flex-1 max-w-xl">
          <div className="flex items-center gap-2 mb-4">
            <span
              className="px-3 py-1 rounded-full text-xs font-semibold uppercase tracking-widest"
              style={{ background: 'rgba(192,132,252,0.15)', color: '#C084FC', border: '1px solid rgba(192,132,252,0.3)' }}
            >
              ★ Destacado
            </span>
            <span
              className="px-3 py-1 rounded-full text-xs font-medium"
              style={{ background: 'rgba(62,7,120,0.4)', color: '#E2E8F0', border: '1px solid rgba(62,7,120,0.5)' }}
            >
              {game.genre}
            </span>
          </div>

          <h1
            className="text-5xl sm:text-6xl font-extrabold text-white leading-tight mb-4"
            style={{ fontFamily: 'Oxanium, sans-serif', letterSpacing: '-0.02em' }}
          >
            {game.title}
          </h1>

          <p className="text-sm mb-2" style={{ color: '#7E9BBF' }}>
            por <span style={{ color: '#C084FC' }}>{game.developer}</span>
          </p>

          <p className="text-base leading-relaxed mb-6 max-w-md" style={{ color: '#94A3B8' }}>
            {game.description}
          </p>

          {/* Rating */}
          <div className="flex items-center gap-4 mb-6">
            <div className="flex items-center gap-2">
              <span className="text-3xl font-bold" style={{ fontFamily: 'Oxanium, sans-serif', color: '#C084FC' }}>
                {game.rating.toFixed(1)}
              </span>
              <div>
                <StarRow rating={game.rating} />
                <span className="text-xs" style={{ color: '#7E9BBF' }}>{game.votes.toLocaleString('es-ES')} votos</span>
              </div>
            </div>
            <div style={{ width: 1, height: 40, background: 'rgba(62,7,120,0.5)' }} />
            <div>
              <p className="text-2xl font-bold text-white" style={{ fontFamily: 'Oxanium, sans-serif' }}>
                ${game.price.toFixed(2)}
              </p>
              <p className="text-xs" style={{ color: '#7E9BBF' }}>USD</p>
            </div>
          </div>
        </div>

        {/* Cover image */}
        <div className="hidden lg:block shrink-0">
          <div
            className="relative rounded-2xl overflow-hidden"
            style={{
              width: 220,
              height: 308,
              boxShadow: '0 0 60px rgba(192,132,252,0.25), 0 20px 60px rgba(0,0,0,0.5)',
              border: '1px solid rgba(192,132,252,0.2)',
            }}
          >
            <img
              src={game.coverImage}
              alt={game.title}
              className="w-full h-full object-cover"
            />
            <div
              className="absolute inset-0"
              style={{ background: 'linear-gradient(to top, rgba(7,27,66,0.6) 0%, transparent 50%)' }}
            />
          </div>
        </div>
      </div>
    </section>
  )
}

function StarRow({ rating }: { rating: number }) {
  return (
    <div className="flex gap-0.5 mb-0.5">
      {[1, 2, 3, 4, 5].map(i => {
        const filled = rating / 2 >= i
        const half = !filled && rating / 2 >= i - 0.5
        return (
          <svg key={i} className="w-3.5 h-3.5" viewBox="0 0 24 24" fill={filled ? '#C084FC' : half ? 'url(#half)' : 'none'} stroke="#C084FC" strokeWidth={1.5}>
            {half && (
              <defs>
                <linearGradient id="half">
                  <stop offset="50%" stopColor="#C084FC" />
                  <stop offset="50%" stopColor="transparent" />
                </linearGradient>
              </defs>
            )}
            <path strokeLinecap="round" strokeLinejoin="round" d="M11.48 3.499a.562.562 0 011.04 0l2.125 5.111a.563.563 0 00.475.345l5.518.442c.499.04.701.663.321.988l-4.204 3.602a.563.563 0 00-.182.557l1.285 5.385a.562.562 0 01-.84.61l-4.725-2.885a.563.563 0 00-.586 0L6.982 20.54a.562.562 0 01-.84-.61l1.285-5.386a.562.562 0 00-.182-.557l-4.204-3.602a.562.562 0 01.321-.988l5.518-.442a.563.563 0 00.475-.345L11.48 3.5z" />
          </svg>
        )
      })}
    </div>
  )
}
