import { useState, useEffect } from 'react'
import { collection, onSnapshot } from 'firebase/firestore'
import { db } from '../firebase'
import { mapFirestoreGame } from '../data/mapper'
import type { Game } from '../data/games'

export function useGames() {
  const [games, setGames] = useState<Game[]>([])
  const [loading, setLoading] = useState(true)

  useEffect(() => {
    const unsubscribe = onSnapshot(collection(db, 'videojuegos'), (snapshot) => {
      const gamesList = snapshot.docs.map(doc => {
        const data = doc.data()
        data.id = doc.id
        return mapFirestoreGame(data as any)
      })
      setGames(gamesList)
      setLoading(false)
    }, (error) => {
      console.error('Error al cargar juegos:', error)
      setLoading(false)
    })

    return () => unsubscribe()
  }, [])

  return { games, loading }
}
