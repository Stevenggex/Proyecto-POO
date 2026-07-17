import { useEffect, useState } from 'react'

interface AuthModalProps {
  isOpen: boolean
  onClose: () => void
  onLoginSuccess: (user: { username: string; role: string }) => void
  onInstallApp: () => void
  activeUser: { username: string; role: string } | null
}

export default function AuthModal({ isOpen, onClose, onLoginSuccess, onInstallApp, activeUser }: AuthModalProps) {
  const [mode, setMode] = useState<'login' | 'register'>('login')
  const [username, setUsername] = useState('')
  const [password, setPassword] = useState('')
  const [error, setError] = useState('')
  const [successMessage, setSuccessMessage] = useState('')
  const [isSubmitting, setIsSubmitting] = useState(false)

  useEffect(() => {
    if (!isOpen) {
      setMode('login')
      setUsername('')
      setPassword('')
      setError('')
      setSuccessMessage('')
      setIsSubmitting(false)
    }
  }, [isOpen])

  if (!isOpen) return null

  const getLocalUsers = () => {
    if (typeof window === 'undefined') return {}

    try {
      const stored = window.localStorage.getItem('gamevault-users')
      return stored ? JSON.parse(stored) : {}
    } catch {
      return {}
    }
  }

  const saveLocalUser = (user: { username: string; password: string; role: string }) => {
    if (typeof window === 'undefined') return

    const users = getLocalUsers()
    users[user.username.toLowerCase()] = user
    window.localStorage.setItem('gamevault-users', JSON.stringify(users))
  }

  const handleLogin = async (event: React.FormEvent<HTMLFormElement>) => {
    event.preventDefault()
    setError('')
    setSuccessMessage('')

    const cleanUsername = username.trim().toLowerCase()
    const cleanPassword = password.trim()

    if (!cleanUsername || !cleanPassword) {
      setError('Ingresa un usuario y una contraseña para continuar.')
      return
    }

    setIsSubmitting(true)

    try {
      const localUsers = getLocalUsers()
      const localUser = localUsers[cleanUsername]

      if (localUser?.password === cleanPassword) {
        setSuccessMessage('¡Inicio de sesión correcto!')
        onLoginSuccess({ username: localUser.username, role: localUser.role })
      } else {
        setError('El usuario o la contraseña no coinciden.')
      }
    } finally {
      setIsSubmitting(false)
    }
  }

  const handleRegister = async (event: React.FormEvent<HTMLFormElement>) => {
    event.preventDefault()
    setError('')
    setSuccessMessage('')

    const cleanUsername = username.trim()
    const cleanPassword = password.trim()

    if (!cleanUsername || !cleanPassword) {
      setError('Ingresa un nombre de usuario y una contraseña.')
      return
    }

    setIsSubmitting(true)

    try {
      const normalizedUsername = cleanUsername.toLowerCase()
      const localUsers = getLocalUsers()

      if (localUsers[normalizedUsername]) {
        setError('Ese usuario ya existe. Elige otro nombre.')
        setIsSubmitting(false)
        return
      }

      saveLocalUser({
        username: cleanUsername,
        password: cleanPassword,
        role: 'Usuario',
      })

      setUsername(cleanUsername)
      setPassword('')
      setMode('login')
      setSuccessMessage('¡Ya estás registrado! Ahora puedes iniciar sesión con tu usuario y contraseña.')
    } catch (error) {
      console.error('Registration error:', error)
      setError('No se pudo completar el registro.')
    } finally {
      setIsSubmitting(false)
    }
  }

  const handleDownloadClick = () => {
    onInstallApp()
  }

  const usernameLabel = mode === 'register' ? 'Nuevo usuario' : 'Usuario'
  const passwordLabel = mode === 'register' ? 'Nueva contraseña' : 'Contraseña'
  const usernamePlaceholder = mode === 'register' ? 'Elige tu usuario' : 'Tu usuario'
  const passwordPlaceholder = mode === 'register' ? 'Elige tu contraseña' : 'Tu contraseña'

  return (
    <div className="fixed inset-0 z-[60] flex items-center justify-center px-4" style={{ background: 'rgba(2,6,23,0.76)' }}>
      <div className="w-full max-w-md rounded-2xl border p-6 shadow-2xl" style={{ background: 'rgba(4,10,107,0.95)', borderColor: 'rgba(192,132,252,0.35)' }}>
        <div className="flex items-start justify-between gap-3">
          <div>
            <p className="text-xs uppercase tracking-[0.3em]" style={{ color: '#C084FC' }}>Acceso</p>
            <h2 className="text-2xl font-semibold text-white mt-1">
              {activeUser ? 'Sesión iniciada' : mode === 'login' ? 'Iniciar sesión' : 'Registrar usuario'}
            </h2>
          </div>
          <button
            type="button"
            onClick={onClose}
            className="rounded-full px-2.5 py-1 text-sm"
            style={{ background: 'rgba(255,255,255,0.08)', color: '#E2E8F0' }}
          >
            ✕
          </button>
        </div>

        {activeUser ? (
          <div className="mt-6 space-y-4">
            <div className="rounded-xl border p-4" style={{ background: 'rgba(62,7,120,0.2)', borderColor: 'rgba(192,132,252,0.25)' }}>
              <p className="text-sm" style={{ color: '#E2E8F0' }}>
                Bienvenido, <span className="font-semibold text-white">{activeUser.username}</span>.
              </p>
              <p className="text-sm mt-2" style={{ color: '#7E9BBF' }}>
                Tu sesión está activa. Si deseas continuar con la experiencia de la app, puedes descargarla cuando quieras.
              </p>
            </div>
            <button
              type="button"
              onClick={handleDownloadClick}
              className="w-full rounded-lg px-4 py-3 font-semibold transition-all"
              style={{ background: 'linear-gradient(135deg, #3E0778, #040A6B)', color: '#F5E8FF' }}
            >
              Descargar App
            </button>
          </div>
        ) : (
          <form onSubmit={mode === 'login' ? handleLogin : handleRegister} className="mt-6 space-y-4">
            <div>
              <label className="text-sm block mb-2" style={{ color: '#E2E8F0' }}>{usernameLabel}</label>
              <input
                value={username}
                onChange={event => setUsername(event.target.value)}
                className="w-full rounded-lg border px-3 py-2.5 text-sm outline-none"
                style={{ background: 'rgba(7,27,66,0.8)', borderColor: 'rgba(62,7,120,0.5)', color: '#F8FAFC' }}
                placeholder={usernamePlaceholder}
              />
            </div>

            <div>
              <label className="text-sm block mb-2" style={{ color: '#E2E8F0' }}>{passwordLabel}</label>
              <input
                type="password"
                value={password}
                onChange={event => setPassword(event.target.value)}
                className="w-full rounded-lg border px-3 py-2.5 text-sm outline-none"
                style={{ background: 'rgba(7,27,66,0.8)', borderColor: 'rgba(62,7,120,0.5)', color: '#F8FAFC' }}
                placeholder={passwordPlaceholder}
              />
            </div>

            {error ? (
              <p className="text-sm" style={{ color: '#FCA5A5' }}>{error}</p>
            ) : null}

            {successMessage ? (
              <p className="text-sm" style={{ color: '#A7F3D0' }}>{successMessage}</p>
            ) : null}

            <button
              type="submit"
              disabled={isSubmitting}
              className="w-full rounded-lg px-4 py-3 font-semibold transition-all disabled:opacity-70"
              style={{ background: 'linear-gradient(135deg, #3E0778, #040A6B)', color: '#F5E8FF' }}
            >
              {isSubmitting ? 'Procesando...' : mode === 'login' ? 'Iniciar sesión' : 'Registrar'}
            </button>

            <div className="flex items-center justify-center pt-2">
              <button
                type="button"
                onClick={() => {
                  setMode(mode === 'login' ? 'register' : 'login')
                  setError('')
                }}
                className="text-sm font-medium"
                style={{ color: '#C084FC' }}
              >
                {mode === 'login' ? '¿No tienes cuenta? Regístrate' : 'Volver a iniciar sesión'}
              </button>
            </div>
          </form>
        )}
      </div>
    </div>
  )
}
