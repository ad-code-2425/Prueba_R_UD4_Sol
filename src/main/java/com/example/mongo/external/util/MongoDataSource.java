package com.example.mongo.external.util;



public class MongoDataSource {
    private final String host;
    private final int port;
    private final String database;

    public MongoDataSource(String host, int port, String database) {
        this.host = host;
        this.port = port;
        this.database = database;
    }

    public String getHost() {
        return host;
    }

    public int getPort() {
        return port;
    }

    public String getDatabase() {
        return database;
    }

    public String getConnectionString() {
        return "mongodb://" + host + ":" + port;
    }
}
