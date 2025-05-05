package com.example.mongo;

import java.util.Arrays;

import org.bson.Document;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

public class JavaMongoDbInsert {
  public static void main(String args[]) {
    MongoClient clienteMongo = MongoClients.create();
    // Si no existe, se crea la BD y la colección
    MongoDatabase baseDatos = clienteMongo.getDatabase("centro");
    MongoCollection<Document> coleccion = baseDatos.getCollection("personas");
    Document persona = new Document("nombre", "Sergio")
        .append("tipo", "Profesor")
        .append("datos_personales", new Document("telefono", "123456789")
            .append("email", "su@correo.es"))
        .append("asignaturas", Arrays.asList("Física", "Matemáticas"));
    coleccion.insertOne(persona);
    clienteMongo.close();
  }
}