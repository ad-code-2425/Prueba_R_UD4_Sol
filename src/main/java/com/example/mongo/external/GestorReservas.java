package com.example.mongo.external;

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

public class GestorReservas {

    private static final String COLLECTION_NAME = "reservas";
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {

        String ruta = "src/main/resources/db.properties";
        MongoDataSource ds = ConnectionManager.getDataSource(ruta);

        try (MongoClient client = ConnectionManager.getMongoClient(ds)) {
            MongoDatabase db = client.getDatabase(ds.getDatabase());
            MongoCollection<Document> coleccion = db.getCollection(COLLECTION_NAME);

            pausar(1000);
            while (true) {
                mostrarMenu(db, coleccion);
            }
        }
    }

    private static void pausar(int milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            System.out.println("La pausa fue interrumpida.");
        }
    }

    private static void mostrarMenu(MongoDatabase db, MongoCollection<Document> coleccion) {
        System.out.println("\n----- GESTOR DE RESERVAS -----");
        System.out.println("1. Listar todas las reservas");
        System.out.println("2. Insertar nueva reserva");
        System.out.println("3. Buscar reserva por ID");
        System.out.println("4. Eliminar reserva por ID");
        System.out.println("5. Eliminar colección por nombre");
        System.out.println("6. Salir");
        System.out.print("Selecciona una opción: ");

        String opcion = scanner.nextLine();

        switch (opcion) {
            case "1":
                listarReservas(coleccion);
                break;
            case "2":
                insertarReserva(coleccion);
                break;
            case "3":
                leerReservaPorId(coleccion);
                break;
            case "4":
                eliminarReservaPorId(coleccion);
                break;
            case "5":
                String nombreColeccion = pedirCadenaPorTeclado("Introduce el nombre de la colección a eliminar: ");
                eliminarColeccion(db, nombreColeccion);
                break;
            case "6":
                System.out.println("Cerrando la aplicación...");
                System.exit(0);
            default:
                System.out.println("Opción no válida.");
        }
    }

    private static void listarReservas(MongoCollection<Document> coleccion) {
        System.out.println("\n--- LISTA DE RESERVAS ---");
        FindIterable<Document> reservas = coleccion.find();
        int contador = 1;
        for (Document doc : reservas) {
            System.out.println("Reserva " + contador++);
            System.out.println(doc.toJson());
        }
    }

    private static Document crearReserva() {
        String fecha = pedirCadenaPorTeclado("Fecha (YYYY-MM-DD): ");
        String hora = pedirCadenaPorTeclado("Hora (HH:mm): ");
        int comensales = pedirEnteroPorTeclado("Número de comensales: ");
        String telefono = pedirCadenaPorTeclado("Teléfono: ");
        String responsable = pedirCadenaPorTeclado("Responsable de la reserva: ");

        return new Document("fecha", fecha)
                .append("hora", hora)
                .append("numeroComensales", comensales)
                .append("telefono", telefono)
                .append("responsableReserva", responsable);
    }

    private static void insertarReserva(MongoCollection<Document> coleccion) {
        Document reserva = crearReserva();
        coleccion.insertOne(reserva);
        System.out.println("Reserva insertada correctamente.");
    }

    private static void leerReservaPorId(MongoCollection<Document> coleccion) {
        System.out.print("Introduce el Object ID de la reserva: ");
        String idStr = scanner.nextLine();

        try {
            ObjectId id = new ObjectId(idStr);
            buscarReservaPorId(id, coleccion);
        } catch (IllegalArgumentException e) {
            System.out.println("ID no válido.");
        }
    }

    private static void buscarReservaPorId(ObjectId id, MongoCollection<Document> coleccion) {
        Document reserva = coleccion.find(new Document("_id", id)).first();
        if (reserva != null) {
            System.out.println("Reserva encontrada:\n" + reserva.toJson());
        } else {
            System.out.println("No se encontró ninguna reserva con ese ID.");
        }
    }

    private static void eliminarReservaPorId(MongoCollection<Document> coleccion) {
        System.out.print("Introduce el ID de la reserva a eliminar: ");
        String idStr = scanner.nextLine();

        try {
            ObjectId id = new ObjectId(idStr);
            DeleteResult resultado = coleccion.deleteOne(new Document("_id", id));
            if (resultado.getDeletedCount() > 0) {
                System.out.println("Reserva eliminada correctamente.");
            } else {
                System.out.println("No se encontró ninguna reserva con ese ID.");
            }
        } catch (IllegalArgumentException e) {
            System.out.println("ID no válido.");
        }
    }

    private static String pedirCadenaPorTeclado(String mensaje) {
        System.out.print(mensaje);
        return scanner.nextLine().trim();
    }

    private static int pedirEnteroPorTeclado(String mensaje) {
        while (true) {
            try {
                System.out.print(mensaje);
                return Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Por favor, introduce un número válido.");
            }
        }
    }

    private static void eliminarColeccion(MongoDatabase db, String nombreColeccion) {
       
        db.getCollection(nombreColeccion).drop();
        System.out.println("Colección '" + nombreColeccion + "' eliminada.");
    }
}
