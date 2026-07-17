import { initializeApp, getApps } from "firebase/app";
import { getDatabase } from "firebase/database";

const firebaseConfig = {
  apiKey: "AIzaSyDVITbym70fMGh3jX6ygcv4U5pXNFfqTs0",
  authDomain: "tienda-de-videojuegos-23b12.firebaseapp.com",
  projectId: "tienda-de-videojuegos-23b12",
  storageBucket: "tienda-de-videojuegos-23b12.firebasestorage.app",
  messagingSenderId: "815052921177",
  appId: "1:815052921177:web:2d1586c668c6efaf889448"
};

const app = getApps().length ? getApps()[0] : initializeApp(firebaseConfig);

export const db = getDatabase(app);