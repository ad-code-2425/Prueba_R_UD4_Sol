package com.example.mongo;

import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoDatabase;

public class JavaMongoDbConnection {
    public static void main(String args[]) {
        MongoClient clienteMongo = MongoClients.create();
        MongoDatabase baseDatos = clienteMongo.getDatabase("test");
        for (String name : baseDatos.listCollectionNames()) {
            System.out.println(name);
        }
        clienteMongo.close();
    }
}