import { useState } from 'react'

interface HeaderProps {
  searchQuery: string
  onSearch: (q: string) => void
  onRegister: () => void
  onInstallApp: () => void
}

export default function Header({ searchQuery, onSearch, onRegister, onInstallApp }: HeaderProps) {
  const [menuOpen, setMenuOpen] = useState(false)

  return (
    <header
      className="sticky top-0 z-50 border-b"
      style={{
        background: 'rgba(4, 10, 107, 0.85)',
        backdropFilter: 'blur(16px)',
        borderColor: 'rgba(62, 7, 120, 0.5)',
      }}
    >
      <div className="max-w-[1400px] mx-auto px-4 sm:px-6 lg:px-8">
        <div className="flex items-center justify-between h-16 gap-4">
          {/* Logo */}
          <div className="flex items-center gap-3 shrink-0">
            <div
              className="w-9 h-9 rounded-lg flex items-center justify-center text-white font-bold text-sm"
              style={{ background: 'linear-gradient(135deg, #3E0778, #040A6B)' }}
            >
              <svg viewBox="0 0 24 24" fill="currentColor" className="w-5 h-5">
                <path d="M12 2C6.48 2 2 6.48 2 12s4.48 10 10 10 10-4.48 10-10S17.52 2 12 2zm-1 14H9V8h2v8zm4 0h-2V8h2v8z" />
                <path d="M7 10h2v4H7v-4zm8 0h2v4h-2v-4z" opacity=".3" />
              </svg>
            </div>
            <span
              className="text-xl font-bold tracking-wider text-white"
              style={{ fontFamily: 'Oxanium, sans-serif' }}
            >
              GAME<span style={{ color: '#C084FC' }}>VAULT</span>
            </span>
          </div>

          {/* Nav links - desktop */}
          <nav className="hidden md:flex items-center gap-1">
            {['Inicio', 'Catálogo', 'Géneros', 'Plataformas'].map(item => (
              <a
                key={item}
                href="#"
                className="px-4 py-2 rounded-md text-sm font-medium transition-all duration-200"
                style={{ color: '#94A3B8' }}
                onMouseEnter={e => {
                  e.currentTarget.style.color = '#E2E8F0'
                  e.currentTarget.style.background = 'rgba(62,7,120,0.3)'
                }}
                onMouseLeave={e => {
                  e.currentTarget.style.color = '#94A3B8'
                  e.currentTarget.style.background = 'transparent'
                }}
              >
                {item}
              </a>
            ))}
          </nav>

          {/* Search */}
          <div className="flex-1 max-w-xs hidden sm:block">
            <div className="relative">
              <svg
                className="absolute left-3 top-1/2 -translate-y-1/2 w-4 h-4"
                style={{ color: '#7E9BBF' }}
                fill="none" stroke="currentColor" viewBox="0 0 24 24"
              >
                <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M21 21l-6-6m2-5a7 7 0 11-14 0 7 7 0 0114 0z" />
              </svg>
              <input
                type="text"
                placeholder="Buscar juego o desarrollador..."
                value={searchQuery}
                onChange={e => onSearch(e.target.value)}
                className="w-full pl-9 pr-4 py-2 rounded-lg text-sm text-white placeholder-[#7E9BBF] outline-none transition-all"
                style={{
                  background: 'rgba(7,27,66,0.8)',
                  border: '1px solid rgba(62,7,120,0.5)',
                }}
                onFocus={e => { e.currentTarget.style.borderColor = '#C084FC' }}
                onBlur={e => { e.currentTarget.style.borderColor = 'rgba(62,7,120,0.5)' }}
              />
            </div>
          </div>

          {/* Action buttons */}
          <div className="hidden sm:flex items-center gap-2 shrink-0">
            <button
              type="button"
              onClick={onInstallApp}
              className="px-3 py-1.5 rounded-full text-xs font-semibold transition-all duration-200"
              style={{
                background: 'rgba(192,132,252,0.15)',
                border: '1px solid rgba(192,132,252,0.35)',
                color: '#E9D5FF',
              }}
            >
              Instalar App
            </button>
            <button
              type="button"
              onClick={onRegister}
              className="px-3 py-1.5 rounded-full text-xs font-semibold transition-all duration-200"
              style={{
                background: 'linear-gradient(135deg, #3E0778, #040A6B)',
                border: '1px solid rgba(232,121,249,0.35)',
                color: '#F5E8FF',
              }}
            >
              Registro
            </button>
          </div>

          {/* Mobile menu button */}
          <button
            className="md:hidden p-2 rounded-md"
            style={{ color: '#94A3B8' }}
            onClick={() => setMenuOpen(!menuOpen)}
          >
            <svg className="w-6 h-6" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2}
                d={menuOpen ? "M6 18L18 6M6 6l12 12" : "M4 6h16M4 12h16M4 18h16"}
              />
            </svg>
          </button>
        </div>

        {/* Mobile menu */}
        {menuOpen && (
          <div className="md:hidden pb-4 pt-2 space-y-2 border-t" style={{ borderColor: 'rgba(62,7,120,0.3)' }}>
            <div className="pt-2 pb-1">
              <input
                type="text"
                placeholder="Buscar juego..."
                value={searchQuery}
                onChange={e => onSearch(e.target.value)}
                className="w-full pl-4 pr-4 py-2 rounded-lg text-sm text-white placeholder-[#7E9BBF] outline-none"
                style={{ background: 'rgba(7,27,66,0.8)', border: '1px solid rgba(62,7,120,0.5)' }}
              />
            </div>
            {['Inicio', 'Catálogo', 'Géneros', 'Plataformas'].map(item => (
              <a key={item} href="#" className="block px-3 py-2 rounded-md text-sm" style={{ color: '#94A3B8' }}>
                {item}
              </a>
            ))}
          </div>
        )}
      </div>
    </header>
  )
}
