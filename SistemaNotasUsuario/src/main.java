import java.util.Scanner;

public class Main {

    private static final Scanner scanner = new Scanner(System.in);
    private static final UserManager userManager = new UserManager();
    private static final NoteManager noteManager = new NoteManager();

    public static void main(String[] args) {
        FileUtils.initBaseFolders();
        mainMenu();
    }

    private static void mainMenu() {
        int option;
        do {
            System.out.println("----------------------------------");
            System.out.println("   SISTEMA DE NOTAS POR USUARIO   ");
            System.out.println("----------------------------------");
            System.out.println("1. Registrarse");
            System.out.println("2. Iniciar sesión");
            System.out.println("0. Salir");
            System.out.print("Opción: ");

            option = readInt();

            switch (option) {
                case 1 -> register();
                case 2 -> login();
                case 0 -> System.out.println("Saliendo del sistema...");
                default -> System.out.println("Opción no válida.");
            }
        } while (option != 0);
    }

    private static void register() {
        System.out.println("---- REGISTRO ----");
        System.out.print("Email: ");
        String email = scanner.nextLine().trim();
        System.out.print("Contraseña: ");
        String password = scanner.nextLine().trim();

        if (email.isEmpty() || password.isEmpty()) {
            System.out.println("Email y contraseña no pueden estar vacíos.");
            return;
        }

        boolean ok = userManager.registerUser(email, password);
        if (ok) {
            System.out.println("Usuario registrado correctamente.");
        } else {
            System.out.println("No se pudo registrar (¿usuario ya existe?).");
        }
    }

    private static void login() {
        System.out.println("---- LOGIN ----");
        System.out.print("Email: ");
        String email = scanner.nextLine().trim();
        System.out.print("Contraseña: ");
        String password = scanner.nextLine().trim();

        if (userManager.validateLogin(email, password)) {
            System.out.println("Login correcto. Bienvenido, " + email);
            userMenu(email);
        } else {
            System.out.println("Credenciales incorrectas o usuario no existe.");
        }
    }

    private static void userMenu(String email) {
        int option;
        do {
            System.out.println("----------------------------------");
            System.out.println("   MENÚ DE USUARIO - " + email);
            System.out.println("----------------------------------");
            System.out.println("1. Crear nota");
            System.out.println("2. Listar notas");
            System.out.println("3. Ver nota por número");
            System.out.println("4. Eliminar nota por número");
            System.out.println("0. Cerrar sesión");
            System.out.print("Opción: ");

            option = readInt();

            switch (option) {
                case 1 -> createNote(email);
                case 2 -> listNotes(email);
                case 3 -> viewNote(email);
                case 4 -> deleteNote(email);
                case 0 -> System.out.println("Cerrando sesión...");
                default -> System.out.println("Opción no válida.");
            }
        } while (option != 0);
    }

    private static void createNote(String email) {
        System.out.println("---- CREAR NOTA ----");
        System.out.print("Título: ");
        String title = scanner.nextLine().trim();
        System.out.print("Contenido: ");
        String content = scanner.nextLine().trim();

        if (title.isEmpty() || content.isEmpty()) {
            System.out.println("Título y contenido no pueden estar vacíos.");
            return;
        }

        noteManager.addNote(email, title, content);
        System.out.println("Nota guardada correctamente.");
    }

    private static void listNotes(String email) {
        System.out.println("---- LISTAR NOTAS ----");
        noteManager.listNotes(email);
    }

    private static void viewNote(String email) {
        System.out.println("---- VER NOTA ----");
        System.out.print("Número de nota: ");
        int index = readInt();
        noteManager.viewNote(email, index);
    }

    private static void deleteNote(String email) {
        System.out.println("---- ELIMINAR NOTA ----");
        System.out.print("Número de nota: ");
        int index = readInt();
        noteManager.deleteNote(email, index);
    }

    private static int readInt() {
        while (true) {
            String line = scanner.nextLine();
            try {
                return Integer.parseInt(line.trim());
            } catch (NumberFormatException e) {
                System.out.print("Introduce un número válido: ");
            }
        }
    }
}
