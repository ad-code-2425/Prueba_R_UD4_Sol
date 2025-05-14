package com.example.mongo.external;

import java.util.Arrays;
import java.util.Scanner;

import org.bson.Document;
import org.bson.types.ObjectId;

import com.example.mongo.external.util.ConnectionManager;
import com.example.mongo.external.util.MongoDataSource;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.result.DeleteResult;

public class GestorAlumnado {

    private static final String COLLECTION_NAME = "alumnado";
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {

        // Ruta al archivo properties
        String ruta = "src/main/resources/db.properties";

        MongoDataSource ds = ConnectionManager.getDataSource(ruta);

        try (MongoClient client = ConnectionManager.getMongoClient(ds)) {
            MongoDatabase db = client.getDatabase(ds.getDatabase());

            // try (MongoClient client = MongoClients.create("mongodb://localhost:27017")) {
            // MongoDatabase db = client.getDatabase(DATABASE_NAME);
            MongoCollection<Document> coleccion = db.getCollection(COLLECTION_NAME);

            while (true) {
                mostrarMenu(db, coleccion);
            }

        }
    }

    private static void mostrarMenu(MongoDatabase db, MongoCollection<Document> coleccion) {
        System.out.println("\n----- GESTOR DE ALUMNADO -----");
        System.out.println("1. Listar todos los alumnos");
        System.out.println("2. Insertar alumno (Lucía)");
        System.out.println("3. Buscar alumno por ID");
        System.out.println("4. Eliminar alumno por ID");
         System.out.println("5. Eliminar colección por nombre");
        System.out.println("6. Salir");
        System.out.print("Selecciona una opción: ");

        String opcion = scanner.nextLine();

        switch (opcion) {
            case "1":
                listarAlumnos(coleccion);
                break;
            case "2":
                insertarAlumno(coleccion);
                break;
            case "3":
                leerAlumnoPorId(coleccion);
                break;
                
            case "4":
                eliminarAlumnoPorId(coleccion);
                break;

            case "5":
                
                String nombreColeccion = pedirCadena("Introduce el nombre de la colección a eliminar: ");
               
                eliminarColeccion(db, nombreColeccion);
                break;
            case "6":
                System.out.println("¡Hasta luego!");
                System.exit(0);

            default:
                System.out.println("Opción no válida.");
        }
    }

    // Método reutilizable para pedir cadenas por teclado
    private static String pedirCadena(String mensaje) {
        System.out.print(mensaje);
        return scanner.nextLine().trim();
    }

    private static void eliminarColeccion(MongoDatabase db, String nombreColeccion) {
        db.getCollection(nombreColeccion).drop();
    }

    private static void listarAlumnos(MongoCollection<Document> coleccion) {
        System.out.println("\n--- LISTA DE ALUMNOS ---");
        FindIterable<Document> alumnos = coleccion.find();
        int contador = 1;
        for (Document doc : alumnos) {

            System.out.println(contador + ": " + doc);
        }
    }

    private static Document crearAlumno() {
        Document lucia = new Document("nombre", "Lucía")
                .append("apellidos", "Martínez Gómez")
                .append("fecha_nacimiento", "2006-03-15")
                .append("ciclo", "Desarrollo de Aplicaciones Web")
                .append("curso", "2024-2025")
                .append("modulos", Arrays.asList(
                        new Document("codigo", "DAW101")
                                .append("nombre", "Programación")
                                .append("calificacion_final", 8.5),
                        new Document("codigo", "DAW102")
                                .append("nombre", "Entornos de Desarrollo")
                                .append("calificacion_final", 9.0),
                        new Document("codigo", "DAW103")
                                .append("nombre", "Bases de Datos")
                                .append("calificacion_final", 7.8)));
        return lucia;

    }

    private static void insertarAlumno(MongoCollection<Document> coleccion) {
        Document lucia = crearAlumno();
        coleccion.insertOne(lucia);
        System.out.println("Alumno 'Lucía' insertado correctamente.");
    }

    private static void leerAlumnoPorId(MongoCollection<Document> coleccion) {
        System.out.print("Introduce el Object ID del/la alumno/a: ");
        String idStr = scanner.nextLine();

        try {
            ObjectId id = new ObjectId(idStr);
            buscarAlumnoPorId(id, coleccion);
        } catch (IllegalArgumentException e) {
            System.out.println("ID no válido.");
        }
    }

    private static void buscarAlumnoPorId(ObjectId id, MongoCollection<Document> coleccion) {
        Document alumno = coleccion.find(new Document("_id", id)).first();
        if (alumno != null) {
            System.out.println("Alumno encontrado:\n" + alumno.toJson());
        } else {
            System.out.println("No se encontró ningún alumno con ese ID.");
        }
    }

    private static void eliminarAlumnoPorId(MongoCollection<Document> coleccion) {
        System.out.print("Introduce el ID del alumno a eliminar: ");
        String idStr = scanner.nextLine();

        try {
            ObjectId id = new ObjectId(idStr);
            DeleteResult resultado = coleccion.deleteOne(new Document("_id", id));
            if (resultado != null) {
                System.out.println("Alumno eliminado:\n" + resultado.getDeletedCount());
            } else {
                System.out.println("No se encontró ningún alumno con ese ID.");
            }
        } catch (IllegalArgumentException e) {
            System.out.println("ID no válido.");
        }
    }
}
