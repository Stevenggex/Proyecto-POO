import type { Game, Genre, Platform } from './games'

interface FirestoreGame {
  id: string
  nombre: string
  desarrollador: string
  genero: string
  plataformas: string
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
  return {
    id: parseInt(doc.id) || 0,
    title: doc.nombre,
    developer: doc.desarrollador,
    genre: doc.genero as Genre,
    platforms: doc.plataformas.split(',').map(p => p.trim()) as Platform[],
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
