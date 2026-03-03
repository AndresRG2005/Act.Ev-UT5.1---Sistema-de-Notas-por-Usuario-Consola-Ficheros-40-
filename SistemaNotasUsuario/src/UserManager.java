import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class UserManager {

    private final Path usersFile;

    public UserManager() {
        this.usersFile = FileUtils.getUsersFilePath();
        FileUtils.ensureFileExists(usersFile);
    }

    public boolean registerUser(String email, String password) {
        String sanitized = FileUtils.sanitizeEmail(email);
        if (userExists(email)) {
            return false;
        }

        String hash = HashUtils.hashPassword(password);
        String line = email + ";" + hash;

        try (BufferedWriter writer = Files.newBufferedWriter(usersFile, FileUtils.APPEND_OPTIONS)) {
            writer.write(line);
            writer.newLine();
        } catch (IOException e) {
            System.out.println("Error al guardar el usuario: " + e.getMessage());
            return false;
        }

        
        Path userFolder = FileUtils.getUserFolderPath(sanitized);
        FileUtils.ensureDirectoryExists(userFolder);

        return true;
    }

    public boolean validateLogin(String email, String password) {
        String hash = HashUtils.hashPassword(password);

        try (BufferedReader reader = Files.newBufferedReader(usersFile)) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(";");
                if (parts.length == 2) {
                    String storedEmail = parts[0];
                    String storedHash = parts[1];
                    if (storedEmail.equals(email) && storedHash.equals(hash)) {
                        return true;
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Error al leer usuarios: " + e.getMessage());
        }
        return false;
    }

    private boolean userExists(String email) {
        try (BufferedReader reader = Files.newBufferedReader(usersFile)) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(";");
                if (parts.length >= 1 && parts[0].equals(email)) {
                    return true;
                }
            }
        } catch (IOException e) {
            System.out.println("Error al comprobar usuario: " + e.getMessage());
        }
        return false;
    }
}
