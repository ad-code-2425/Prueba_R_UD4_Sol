package com.example.mongo.external.util;



import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class ConnectionManager {

    private static final String HOST_KEY = "mongodb.host";
    private static final String PORT_KEY = "mongodb.port";
    private static final String DB_KEY   = "mongodb.database";

    private static MongoDataSource dataSource = null;

    private ConnectionManager() {
    }

    public static MongoDataSource getDataSource(String path) {
        if (dataSource == null) {
            try (FileInputStream fis = new FileInputStream(path)) {
                Properties props = new Properties();
                props.load(fis);

                String host = props.getProperty(HOST_KEY);
                int port = Integer.parseInt(props.getProperty(PORT_KEY));
                String db = props.getProperty(DB_KEY);

                dataSource = new MongoDataSource(host, port, db);

            } catch (IOException e) {
                System.err.println("Error cargando el archivo de propiedades: " + e.getMessage());
            } catch (NumberFormatException e) {
                System.err.println("Puerto inválido en db.properties");
            }
        }
        return dataSource;
    }

    public static MongoClient getMongoClient(MongoDataSource ds) {
        return MongoClients.create(ds.getConnectionString());
    }
}

