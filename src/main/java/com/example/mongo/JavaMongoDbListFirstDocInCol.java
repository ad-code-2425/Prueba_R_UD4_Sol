package com.example.mongo;

import org.bson.Document;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
public class JavaMongoDbListFirstDocInCol {
    public static void main( String args[] ) {
        MongoClient clienteMongo = MongoClients.create();
        MongoDatabase baseDatos = clienteMongo.getDatabase("test");
        MongoCollection<Document> coleccion =
        baseDatos.getCollection("amigos");
        Document primero = coleccion.find().first();
        System.out.println(primero.toJson());
        clienteMongo.close();
   }
}