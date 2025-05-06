package com.example.mongo.external;


import com.example.mongo.external.util.ConnectionManager;
import com.example.mongo.external.util.MongoDataSource;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoDatabase;

public class JavaMongoDbConnection {
    public static void main(String args[]) {

        // Ruta al archivo properties
        String ruta = "src/main/resources/db.properties";

        MongoDataSource ds = ConnectionManager.getDataSource(ruta);

        try (MongoClient client = ConnectionManager.getMongoClient(ds)) {
            MongoDatabase db = client.getDatabase(ds.getDatabase());

            for (String name : db.listCollectionNames()) {
                System.out.println(name);
            }
        } catch (Exception e) {
            System.err.println("Error al conectar con MongoDB: " + e.getMessage());
        }

    }
}