package modelo;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.cloud.firestore.Firestore;
import com.google.firebase.cloud.FirestoreClient;

import java.io.FileInputStream;
import java.io.IOException;

public class FirebaseConfig {
    private static Firestore db;

    public static void initFirebase() throws IOException {
        FileInputStream serviceAccount = new FileInputStream("src/main/resources/tienda-de-videojuegos-23b12-firebase-adminsdk-fbsvc-01815cb77c.json");

        FirebaseOptions options = FirebaseOptions.builder()
                .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                .build();

        FirebaseApp.initializeApp(options);
        db = FirestoreClient.getFirestore();
    }

    public static Firestore getDb() {
        return db;
    }
}
