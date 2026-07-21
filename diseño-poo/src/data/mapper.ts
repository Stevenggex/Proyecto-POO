import type { Game, Genre } from './games'

interface FirestoreGame {
  id: string
  nombre: string
  desarrollador: string
  genero: string
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
    id: doc.id,
    title: doc.nombre,
    developer: doc.desarrollador,
    genre: doc.genero as Genre,
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
