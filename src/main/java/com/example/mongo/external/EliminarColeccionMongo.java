package com.example.mongo.external;

import com.example.mongo.external.util.ConnectionManager;
import com.example.mongo.external.util.MongoDataSource;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoDatabase;


public class EliminarColeccionMongo {

    public static void main(String[] args) {
        // Verificamos que se pase el nombre de la colección
        if (args.length < 1) {
            System.out.println("Uso: java EliminarColeccionMongo <nombre_coleccion>");
            return;
        }

        String nombreColeccion = args[0];
        String rutaProperties = "src/main/resources/db.properties"; 

        MongoDataSource ds = ConnectionManager.getDataSource(rutaProperties);

        try (MongoClient client = ConnectionManager.getMongoClient(ds)) {
            MongoDatabase db = client.getDatabase(ds.getDatabase());

           boolean existe = false;
for (String nombre : db.listCollectionNames()) {
    if (nombre.equals(nombreColeccion)) {
        existe = true;
        break;
    }
}

if (existe) {
    db.getCollection(nombreColeccion).drop();
    System.out.println("Colección eliminada: " + nombreColeccion);
} else {
    System.out.println("La colección no existe: " + nombreColeccion);
}

        } catch (Exception e) {
            System.err.println("Error al eliminar la colección: " + e.getMessage());
        }
    }
}

