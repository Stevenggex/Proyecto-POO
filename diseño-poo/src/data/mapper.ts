import type { Game, Genre, Platform } from './games'

interface FirestoreGame {
  id: string
  nombre: string
  desarrollador: string
  genero: string
  plataformas: string | string[]
  precio: number
  cantidad: number
  stock: boolean
  descripcion: string
  imagenPath: string
  fechaLanzamiento: string
  puntuacion: number
  votos: number
  destacado: boolean
}

export function mapFirestoreGame(doc: FirestoreGame): Game {
  const platforms = Array.isArray(doc.plataformas)
    ? doc.plataformas.map(p => p.trim())
    : doc.plataformas
      .split(',')
      .map(p => p.trim())

  return {
    id: doc.id,
    title: doc.nombre,
    developer: doc.desarrollador,
    genre: doc.genero as Genre,
    platforms: platforms as Platform[],
    price: doc.precio,
    rating: doc.puntuacion,
    votes: doc.votos,
    stock: doc.stock,
    releaseDate: doc.fechaLanzamiento,
    description: doc.descripcion,
    coverImage: doc.imagenPath,
    featured: doc.destacado,
  }
}
