import { useEffect, useState } from 'react'
import { addDoc, collection, doc, getDoc, getDocs, query, setDoc, where } from 'firebase/firestore'
import { db } from '../firebase'

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

  const findUserByUsername = async (cleanUsername: string) => {
    const usersRef = collection(db, 'Usuario')
    let usersQuery = query(usersRef, where('usuarioLower', '==', cleanUsername))
    let usersSnapshot = await getDocs(usersQuery)

    if (usersSnapshot.empty) {
      usersQuery = query(usersRef, where('usuario', '==', cleanUsername))
      usersSnapshot = await getDocs(usersQuery)
    }

    return usersSnapshot.docs[0]
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
      const userDoc = await findUserByUsername(cleanUsername)
      const localUser = userDoc?.data()

      if (localUser?.password === cleanPassword) {
        setSuccessMessage('¡Inicio de sesión correcto!')
        onLoginSuccess({ username: localUser.usuario ?? localUser.username, role: localUser.rol ?? localUser.role })
      } else {
        setError('El usuario o la contraseña no coinciden.')
      }
    } catch (error) {
      console.error('Login error:', error)
      setError('No se pudo completar el inicio de sesión.')
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
      const existingUserDoc = await findUserByUsername(normalizedUsername)

      if (existingUserDoc) {
        setError('Ese usuario ya existe. Elige otro nombre.')
        setIsSubmitting(false)
        return
      }

      const usersRef = collection(db, 'Usuario')
      await addDoc(usersRef, {
        usuario: cleanUsername,
        usuarioLower: normalizedUsername,
        password: cleanPassword,
        rol: 'Usuario',
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
