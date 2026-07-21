import { useMemo, useState } from 'react'
import Header from './components/Header'
import HeroBanner from './components/HeroBanner'
import FilterBar from './components/FilterBar'
import GameGrid from './components/GameGrid'
import Footer from './components/Footer'
import AuthModal from './components/AuthModal'
import { useGames } from './hooks/useGames'
import type { Game, Genre } from './data/games'
import { genres as allGenres } from './data/games'

function StatsBar({ games }: { games: Game[] }) {
  const total = games.length
  const genres = new Set(games.map(g => g.genre)).size
  const inStock = games.filter(g => g.stock).length

  const stats = [
    { label: 'Juegos en catálogo', value: total, icon: '🎮' },
    { label: 'Géneros disponibles', value: genres, icon: '🗂️' },
    { label: 'En stock', value: inStock, icon: '✅' },
  ]

  return (
    <div
      className="border-b"
      style={{ borderColor: 'rgba(62,7,120,0.3)', background: 'rgba(4,10,107,0.25)' }}
    >
      <div className="max-w-[1400px] mx-auto px-4 sm:px-6 lg:px-8">
        <div className="grid grid-cols-2 sm:grid-cols-3 divide-x"
          style={{ divideColor: 'rgba(62,7,120,0.3)' }}
        >
          {stats.map((s, i) => (
            <div
              key={i}
              className="py-4 px-6 text-center"
              style={{ borderRight: i < stats.length - 1 ? '1px solid rgba(62,7,120,0.3)' : 'none' }}
            >
              <span className="text-xl">{s.icon}</span>
              <p
                className="text-2xl font-extrabold text-white mt-1"
                style={{ fontFamily: 'Oxanium, sans-serif', color: '#C084FC' }}
              >
                {s.value}
              </p>
              <p className="text-xs mt-0.5" style={{ color: '#7E9BBF' }}>{s.label}</p>
            </div>
          ))}
        </div>
      </div>
    </div>
  )
}

export default function App() {
  const [selectedGenre, setSelectedGenre] = useState<Genre | 'Todos'>('Todos')
  const [sortBy, setSortBy] = useState<'rating' | 'price-asc' | 'price-desc' | 'title'>('rating')
  const [searchQuery, setSearchQuery] = useState('')
  const [isAuthOpen, setIsAuthOpen] = useState(false)
  const [activeUser, setActiveUser] = useState<{ username: string; role: string } | null>(null)
  const { games, loading } = useGames()

  const availableGenres = useMemo(() => {
    const setGenres = new Set<Genre>(allGenres)
    games.forEach(game => setGenres.add(game.genre))
    return Array.from(setGenres) as Genre[]
  }, [games])

  const featuredGame = games.find(g => g.featured) ?? games[0] ?? null

  const filtered = games
    .filter(g => selectedGenre === 'Todos' || g.genre === selectedGenre)
    .filter(g =>
      g.title.toLowerCase().includes(searchQuery.toLowerCase()) ||
      g.developer.toLowerCase().includes(searchQuery.toLowerCase())
    )
    .sort((a, b) => {
      if (sortBy === 'rating') return b.rating - a.rating
      if (sortBy === 'price-asc') return a.price - b.price
      if (sortBy === 'price-desc') return b.price - a.price
      return a.title.localeCompare(b.title)
    })

  const handleAuthOpen = () => setIsAuthOpen(true)
  const handleAuthClose = () => setIsAuthOpen(false)
  const handleLoginSuccess = (user: { username: string; role: string }) => {
    setActiveUser(user)
    setIsAuthOpen(false)
  }

  const handleInstallApp = () => {
    window.alert('La instalación de la app aún no está habilitada.')
  }

  return (
    <div style={{ fontFamily: 'Inter, sans-serif' }} className="min-h-screen flex flex-col">
      <Header searchQuery={searchQuery} onSearch={setSearchQuery} onRegister={handleAuthOpen} onInstallApp={handleAuthOpen} />
      {featuredGame ? <HeroBanner game={featuredGame} /> : null}
      <StatsBar games={games} />
      <main className="flex-1 max-w-[1400px] w-full mx-auto px-4 sm:px-6 lg:px-8 pb-20">
        <FilterBar
          selectedGenre={selectedGenre}
          sortBy={sortBy}
          onGenreChange={setSelectedGenre}
          onSortChange={setSortBy}
          totalResults={filtered.length}
          availableGenres={availableGenres}
        />
        {loading ? (
          <div className="text-center py-20 text-white">Cargando juegos...</div>
        ) : (
          <GameGrid games={filtered} />
        )}
      </main>
      <Footer />
      <AuthModal
        isOpen={isAuthOpen}
        onClose={handleAuthClose}
        onLoginSuccess={handleLoginSuccess}
        onInstallApp={handleInstallApp}
        activeUser={activeUser}
      />
    </div>
  )
}
