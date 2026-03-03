import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

public class FileUtils {

    private static final Path DATA_FOLDER = Paths.get("data");
    private static final Path USERS_FILE = DATA_FOLDER.resolve("users.txt");
    private static final Path USERS_FOLDER = DATA_FOLDER.resolve("usuarios");

    public static final StandardOpenOption[] APPEND_OPTIONS = {
            StandardOpenOption.CREATE,
            StandardOpenOption.APPEND
    };

    public static void initBaseFolders() {
        ensureDirectoryExists(DATA_FOLDER);
        ensureDirectoryExists(USERS_FOLDER);
        ensureFileExists(USERS_FILE);
    }

    public static Path getUsersFilePath() {
        return USERS_FILE;
    }

    public static Path getUserFolderPath(String sanitizedEmail) {
        return USERS_FOLDER.resolve(sanitizedEmail);
    }

    public static void ensureDirectoryExists(Path dir) {
        try {
            if (!Files.exists(dir)) {
                Files.createDirectories(dir);
            }
        } catch (IOException e) {
            System.out.println("Error al crear directorio " + dir + ": " + e.getMessage());
        }
    }

    public static void ensureFileExists(Path file) {
        try {
            if (!Files.exists(file)) {
                if (file.getParent() != null) {
                    ensureDirectoryExists(file.getParent());
                }
                Files.createFile(file);
            }
        } catch (IOException e) {
            System.out.println("Error al crear fichero " + file + ": " + e.getMessage());
        }
    }

    public static String sanitizeEmail(String email) {
        return email.replace("@", "_at_").replace(".", "_dot_");
    }
}
