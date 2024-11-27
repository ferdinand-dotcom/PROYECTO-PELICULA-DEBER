package model;

import java.io.*;
import java.util.*;

public class SistemaContenidoAudiovisual {

    // Definición de la clase base ContenidoAudiovisual
    public static abstract class ContenidoAudiovisual {
        private String nombre;
        private int duracion;

        public ContenidoAudiovisual(String nombre, int duracion) {
            this.nombre = nombre;
            this.duracion = duracion;
        }

        public String getNombre() {
            return nombre;
        }

        public void setNombre(String nombre) {
            this.nombre = nombre;
        }

        public int getDuracion() {
            return duracion;
        }

        public void setDuracion(int duracion) {
            this.duracion = duracion;
        }

        public abstract void mostrarInfo();
    }

    // Clase Pelicula que extiende de ContenidoAudiovisual
    public static class Pelicula extends ContenidoAudiovisual {
        private String director;

        public Pelicula(String nombre, int duracion, String director) {
            super(nombre, duracion);
            this.director = director;
        }

        @Override
        public void mostrarInfo() {
            System.out.println(
                    "Película: " + getNombre() + ", Duración: " + getDuracion() + " minutos, Director: " + director);
        }

        public String getDirector() {
            return director;
        }

        public void setDirector(String director) {
            this.director = director;
        }
    }

    // Clase que maneja la lectura y escritura de archivos
    public static class ArchivoUtil {
        public static void escribirArchivo(String nombreArchivo, List<ContenidoAudiovisual> contenidos) {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(nombreArchivo))) {
                for (ContenidoAudiovisual contenido : contenidos) {
                    writer.write(contenido.getNombre() + "," + contenido.getDuracion());
                    if (contenido instanceof Pelicula) {
                        writer.write("," + ((Pelicula) contenido).getDirector());
                    }
                    writer.newLine();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public static List<ContenidoAudiovisual> leerArchivo(String nombreArchivo) {
            List<ContenidoAudiovisual> contenidos = new ArrayList<>();
            try (BufferedReader reader = new BufferedReader(new FileReader(nombreArchivo))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] datos = line.split(",");
                    String nombre = datos[0];
                    int duracion = Integer.parseInt(datos[1]);
                    if (datos.length == 3) {
                        contenidos.add(new Pelicula(nombre, duracion, datos[2]));
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return contenidos;
        }
    }

    // Controlador que maneja las interacciones entre la vista y el modelo
    public static class ControladorContenidoAudiovisual {
        private List<ContenidoAudiovisual> contenidos;

        public ControladorContenidoAudiovisual() {
            // Acceder al método estático correctamente
            contenidos = ArchivoUtil.leerArchivo("contenidos.csv");
        }

        public void agregarContenido(ContenidoAudiovisual contenido) {
            contenidos.add(contenido);
            ArchivoUtil.escribirArchivo("contenidos.csv", contenidos);
        }

        public void mostrarContenidos() {
            for (ContenidoAudiovisual contenido : contenidos) {
                contenido.mostrarInfo();
            }
        }
    }

    // Vista para mostrar el menú y permitir la interacción con el usuario
    public static class Vista {
        private ControladorContenidoAudiovisual controlador;

        public Vista(ControladorContenidoAudiovisual controlador) {
            this.controlador = controlador;
        }

        @SuppressWarnings("resource")
        public void mostrarMenu() {
            Scanner scanner = new Scanner(System.in);
            while (true) {
                System.out.println("1. Mostrar contenidos");
                System.out.println("2. Agregar película");
                System.out.println("3. Salir");
                System.out.print("Selecciona una opción: ");
                int opcion = scanner.nextInt();
                scanner.nextLine(); // Consumir el salto de línea

                switch (opcion) {
                    case 1:
                        controlador.mostrarContenidos();
                        break;
                    case 2:
                        System.out.print("Nombre de la película: ");
                        String nombre = scanner.nextLine();
                        System.out.print("Duración (en minutos): ");
                        int duracion = scanner.nextInt();
                        scanner.nextLine();
                        System.out.print("Director: ");
                        String director = scanner.nextLine();
                        controlador.agregarContenido(new Pelicula(nombre, duracion, director));
                        break;
                    case 3:
                        System.exit(0);
                        break;
                    default:
                        System.out.println("Opción no válida.");
                }
            }
        }
    }

    // Clase principal para ejecutar el sistema
    public static void main(String[] args) {
        ControladorContenidoAudiovisual controlador = new ControladorContenidoAudiovisual();
        Vista vista = new Vista(controlador);
        vista.mostrarMenu();
    }
}
