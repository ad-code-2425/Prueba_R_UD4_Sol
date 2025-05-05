package com.example.mongo;

import org.bson.Document;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import java.util.Iterator;

public class JavaMongoDbConsulta {
  public static void main(String args[]) {
    MongoClient clienteMongo = MongoClients.create();
    MongoDatabase baseDatos = clienteMongo.getDatabase("test");
    MongoCollection<Document> coleccion = baseDatos.getCollection("amigos");
    // Obtener un objecto iterable de tipo cursor
    FindIterable<Document> iterDoc = coleccion.find(new Document("nombre", "Marleni"));
    int i = 1;
    // Iniciamos el iterador
    Iterator<Document> it = iterDoc.iterator();
    while (it.hasNext()) {
      System.out.println(String.valueOf(i) + it.next());
      i++;
    }
    clienteMongo.close();
  }
}