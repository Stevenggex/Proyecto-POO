export default function Footer() {
  return (
    <footer
      className="mt-8"
      style={{
        background: 'rgba(4,10,107,0.4)',
        borderTop: '1px solid rgba(62,7,120,0.35)',
        backdropFilter: 'blur(8px)',
      }}
    >
      <div className="max-w-[1400px] mx-auto px-4 sm:px-6 lg:px-8 py-8">
        <div className="flex flex-col sm:flex-row items-center justify-between gap-4">
          {/* Logo */}
          <div className="flex items-center gap-3">
            <div
              className="w-8 h-8 rounded-lg flex items-center justify-center"
              style={{ background: 'linear-gradient(135deg, #3E0778, #040A6B)' }}
            >
              <svg viewBox="0 0 24 24" fill="white" className="w-4 h-4">
                <path d="M12 2C6.48 2 2 6.48 2 12s4.48 10 10 10 10-4.48 10-10S17.52 2 12 2zm-1 14H9V8h2v8zm4 0h-2V8h2v8z" />
              </svg>
            </div>
            <span
              className="text-lg font-bold tracking-wider text-white"
              style={{ fontFamily: 'Oxanium, sans-serif' }}
            >
              GAME<span style={{ color: '#C084FC' }}>VAULT</span>
            </span>
          </div>

          {/* Info */}
          <div className="text-center sm:text-right">
            <p className="text-sm flex items-center gap-2 justify-center sm:justify-end" style={{ color: '#7E9BBF' }}>
              <svg className="w-4 h-4 shrink-0" style={{ color: '#C084FC' }} fill="currentColor" viewBox="0 0 24 24">
                <path d="M12 15a3 3 0 100-6 3 3 0 000 6z" />
                <path fillRule="evenodd" d="M1.323 11.447C2.811 6.976 7.028 3.75 12.001 3.75c4.97 0 9.185 3.223 10.675 7.69.12.362.12.752 0 1.113-1.487 4.471-5.705 7.697-10.677 7.697-4.97 0-9.186-3.223-10.675-7.69a1.762 1.762 0 010-1.113zM17.25 12a5.25 5.25 0 11-10.5 0 5.25 5.25 0 0110.5 0z" clipRule="evenodd" />
              </svg>
              Catálogo gestionado desde la aplicación de escritorio · Solo lectura
            </p>
            <p className="text-xs mt-1" style={{ color: '#4A6898' }}>
              Los datos se sincronizan automáticamente desde el backend.
            </p>
          </div>
        </div>
      </div>
    </footer>
  )
}
