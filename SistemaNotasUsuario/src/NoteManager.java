import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class NoteManager {

    public void addNote(String email, String title, String content) {
        Path notesFile = getNotesFile(email);
        FileUtils.ensureFileExists(notesFile);

        String line = title + ";" + content;

        try (BufferedWriter writer = Files.newBufferedWriter(notesFile, FileUtils.APPEND_OPTIONS)) {
            writer.write(line);
            writer.newLine();
        } catch (IOException e) {
            System.out.println("Error al guardar la nota: " + e.getMessage());
        }
    }

    public void listNotes(String email) {
        Path notesFile = getNotesFile(email);
        if (!Files.exists(notesFile)) {
            System.out.println("No hay notas todavía.");
            return;
        }

        try (BufferedReader reader = Files.newBufferedReader(notesFile)) {
            String line;
            int index = 1;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(";", 2);
                String title = parts.length > 0 ? parts[0] : "(sin título)";
                System.out.println(index + ". " + title);
                index++;
            }
            if (index == 1) {
                System.out.println("No hay notas.");
            }
        } catch (IOException e) {
            System.out.println("Error al leer notas: " + e.getMessage());
        }
    }

    public void viewNote(String email, int index) {
        List<String> notes = readAllNotes(email);
        if (index < 1 || index > notes.size()) {
            System.out.println("Índice de nota no válido.");
            return;
        }
        String line = notes.get(index - 1);
        String[] parts = line.split(";", 2);
        String title = parts.length > 0 ? parts[0] : "(sin título)";
        String content = parts.length > 1 ? parts[1] : "";
        System.out.println("---- NOTA " + index + " ----");
        System.out.println("Título: " + title);
        System.out.println("Contenido: " + content);
    }

    public void deleteNote(String email, int index) {
        List<String> notes = readAllNotes(email);
        if (index < 1 || index > notes.size()) {
            System.out.println("Índice de nota no válido.");
            return;
        }
        notes.remove(index - 1);

        Path notesFile = getNotesFile(email);
        try (BufferedWriter writer = Files.newBufferedWriter(notesFile)) {
            for (String line : notes) {
                writer.write(line);
                writer.newLine();
            }
            System.out.println("Nota eliminada correctamente.");
        } catch (IOException e) {
            System.out.println("Error al reescribir notas: " + e.getMessage());
        }
    }

    private List<String> readAllNotes(String email) {
        List<String> notes = new ArrayList<>();
        Path notesFile = getNotesFile(email);
        if (!Files.exists(notesFile)) {
            return notes;
        }

        try (BufferedReader reader = Files.newBufferedReader(notesFile)) {
            String line;
            while ((line = reader.readLine()) != null) {
                notes.add(line);
            }
        } catch (IOException e) {
            System.out.println("Error al leer notas: " + e.getMessage());
        }
        return notes;
    }

    private Path getNotesFile(String email) {
        String sanitized = FileUtils.sanitizeEmail(email);
        Path userFolder = FileUtils.getUserFolderPath(sanitized);
        FileUtils.ensureDirectoryExists(userFolder);
        return userFolder.resolve("notas.txt");
    }
}
