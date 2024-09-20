import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.Scanner;

public class Main {
    private static String ruta = "C:/";  // Directorio actual
    private static File file = new File(ruta);  // Archivo que representa el directorio actual
    private static Scanner sc = new Scanner(System.in);  // Escáner para la entrada del usuario

    public static void main(String[] args) {
        System.out.println("CMD en java");
        System.out.println("------------");
        String comando = "";

        while (!comando.equals("exit")) {
            comando = sc.nextLine();
            procesarComando(comando);
        }
    }

    private static void procesarComando(String comando) {
        String[] partes = comando.split(" ");
        String accion = partes[0];

        switch (accion) {
            case "test":
                mostrarRutaActual();
                break;
            case "ls":
                listarDirectorios();
                break;
            case "cd":
                if (partes.length > 1) cambiarDirectorio(partes[1]);
                else System.out.println("Error: Directorio no especificado");
                break;
            case "mkdir":
                if (partes.length > 1) crearDirectorio(partes[1]);
                else System.out.println("Error: Nombre de directorio no especificado");
                break;
            case "touch":
                if (partes.length > 1) crearArchivo(partes[1]);
                else System.out.println("Error: Nombre de archivo no especificado");
                break;
            case "cat":
                if (partes.length > 1) mostrarContenidoArchivo(partes[1]);
                else System.out.println("Error: Nombre de archivo no especificado");
                break;
            case "write":
                if (partes.length > 1) escribirEnArchivo(partes[1]);
                else System.out.println("Error: Nombre de archivo no especificado");
                break;
            case "delete":
                if (partes.length > 1) eliminarArchivoODirectorio(partes[1]);
                else System.out.println("Error: Nombre de archivo o directorio no especificado");
                break;
            case "pwd":
                mostrarRutaActual();
                break;
            case "rename":
                if (partes.length == 3) renombrarArchivoODirectorio(partes[1], partes[2]);
                else System.out.println("Error: Debes proporcionar el nombre actual y el nuevo nombre");
                break;
            case "help":
                mostrarAyuda();
                break;
            case "man":
                if (partes.length > 1) mostrarManual(partes[1]);
                else System.out.println("Error: Comando no especificado para man");
                break;
            case "exit":
                System.out.println("Gracias por usar el programa, ahora se cerrará");
                break;
            default:
                System.out.println("Error, comando no esperado");
        }
    }

    private static void mostrarRutaActual() {
        System.out.println("Directorio actual: " + ruta);
    }

    private static void listarDirectorios() {
        for (String f : Objects.requireNonNull(file.list())) {
            System.out.println(f);
        }
    }

    private static void cambiarDirectorio(String directorio) {
        if (directorio.equals("..")) {
            File parentDir = file.getParentFile();
            if (parentDir != null) {
                ruta = parentDir.getAbsolutePath();
                file = new File(ruta);
            } else {
                System.out.println("Error: No se puede ir más atrás");
            }
        } else {
            File newDir = new File(file, directorio);
            if (!newDir.isAbsolute()) {
                newDir = new File(directorio);
            }

            if (newDir.exists() && newDir.isDirectory()) {
                ruta = newDir.getAbsolutePath();
                file = new File(ruta);
            } else {
                System.out.println("Error: El directorio no existe");
            }
        }
        mostrarRutaActual();
    }

    private static void crearDirectorio(String nombre) {
        File newDir = new File(ruta + "/" + nombre);
        if (!newDir.exists()) {
            if (newDir.mkdir()) {
                System.out.println("Directorio creado: " + newDir.getAbsolutePath());
            } else {
                System.out.println("Error al crear el directorio");
            }
        } else {
            System.out.println("El directorio ya existe");
        }
    }

    private static void crearArchivo(String nombre) {
        File newFile = new File(ruta + "/" + nombre);
        try {
            if (newFile.createNewFile()) {
                System.out.println("Archivo creado: " + newFile.getAbsolutePath());
            } else {
                System.out.println("El archivo ya existe");
            }
        } catch (IOException e) {
            System.out.println("Error al crear el archivo");
        }
    }

    private static void mostrarContenidoArchivo(String nombre) {
        File fileToRead = new File(ruta + "/" + nombre);
        if (fileToRead.exists() && fileToRead.isFile()) {
            try {
                String content = new String(Files.readAllBytes(Paths.get(fileToRead.getAbsolutePath())));
                System.out.println("Contenido del archivo:");
                System.out.println(content);
            } catch (IOException e) {
                System.out.println("Error al leer el archivo");
            }
        } else {
            System.out.println("El archivo no existe");
        }
    }

    private static void escribirEnArchivo(String nombre) {
        File fileToWrite = new File(ruta + "/" + nombre);
        if (fileToWrite.exists() && fileToWrite.isFile()) {
            System.out.println("Escriba el contenido (escriba EOF para finalizar):");
            StringBuilder content = new StringBuilder();
            while (true) {
                String inputLine = sc.nextLine();
                if (inputLine.equals("EOF")) {
                    break;
                }
                content.append(inputLine).append("\n");
            }
            try (FileWriter fw = new FileWriter(fileToWrite, true)) {
                fw.write(content.toString());
                System.out.println("Contenido escrito en el archivo");
            } catch (IOException e) {
                System.out.println("Error al escribir en el archivo");
            }
        } else {
            System.out.println("El archivo no existe");
        }
    }

    private static void eliminarArchivoODirectorio(String nombre) {
        File fileToDelete = new File(ruta + "/" + nombre);
        if (fileToDelete.exists()) {
            if (fileToDelete.isDirectory() && Objects.requireNonNull(fileToDelete.list()).length > 0) {
                System.out.println("Error: El directorio no está vacío");
            } else {
                if (fileToDelete.delete()) {
                    System.out.println("Archivo/Directorio eliminado");
                } else {
                    System.out.println("Error al eliminar el archivo/directorio");
                }
            }
        } else {
            System.out.println("El archivo/directorio no existe");
        }
    }

    private static void renombrarArchivoODirectorio(String oldName, String newName) {
        File fileToRename = new File(ruta + "/" + oldName);
        File newFile = new File(ruta + "/" + newName);

        if (fileToRename.exists()) {
            if (fileToRename.renameTo(newFile)) {
                System.out.println("Renombrado exitoso: " + oldName + " a " + newName);
            } else {
                System.out.println("Error al renombrar el archivo/directorio");
            }
        } else {
            System.out.println("Error: El archivo/directorio no existe");
        }
    }

    private static void mostrarAyuda() {
        System.out.println("Comandos disponibles:");
        System.out.println("ls          - Lista los archivos y carpetas en el directorio actual");
        System.out.println("cd [dir]    - Cambia al directorio especificado");
        System.out.println("mkdir [dir] - Crea un nuevo directorio");
        System.out.println("touch [file]- Crea un nuevo archivo vacío");
        System.out.println("cat [file]  - Muestra el contenido de un archivo");
        System.out.println("write [file]- Escribe contenido en un archivo (escribe 'EOF' para finalizar)");
        System.out.println("delete [file|dir] - Elimina un archivo o directorio");
        System.out.println("rename [old_name] [new_name] - Renombra un archivo o directorio");
        System.out.println("pwd         - Muestra la ruta del directorio actual");
        System.out.println("man [command]- Muestra la descripción de un comando");
        System.out.println("help        - Muestra este menú de ayuda");
        System.out.println("exit        - Salir del programa");
    }

    private static void mostrarManual(String comando) {
        switch (comando) {
            case "ls":
                System.out.println("ls: Lista los archivos y carpetas en el directorio actual.");
                break;
            case "cd":
                System.out.println("cd [dir]: Cambia al directorio especificado. Usa '..' para ir al directorio padre.");
                break;
            case "mkdir":
                System.out.println("mkdir [dir]: Crea un nuevo directorio en el directorio actual.");
                break;
            case "touch":
                System.out.println("touch [file]: Crea un nuevo archivo vacío en el directorio actual.");
                break;
            case "cat":
                System.out.println("cat [file]: Muestra el contenido de un archivo especificado.");
                break;
            case "write":
                System.out.println("write [file]: Escribe contenido en un archivo (escriba 'EOF' para finalizar).");
                break;
            case "delete":
                System.out.println("delete [file|dir]: Elimina un archivo o directorio especificado.");
                break;
            case "rename":
                System.out.println("rename [old_name] [new_name]: Renombra un archivo o directorio.");
                break;
            case "pwd":
                System.out.println("pwd: Muestra la ruta del directorio actual.");
                break;
            case "help":
                System.out.println("help: Muestra este menú de ayuda.");
                break;
            default:
                System.out.println("Error: No se encontró la descripción para el comando '" + comando + "'.");
        }
    }
}
