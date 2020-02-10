import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public abstract class DatabaseManager {
    private static String rootDirectory = "./";

    // maybe remove this
    public static void setRootDirectory(String path) {
        try {
            if(!Files.exists(Paths.get(path))) {
                Files.createDirectory(Paths.get(path));
            }
            rootDirectory = path;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void createDirectory(String path) {
        try {
            if(!Files.exists(Paths.get(rootDirectory+path))) {
                Files.createDirectory(Paths.get(rootDirectory+path));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static File readFile(String path) {
        return new File(rootDirectory+path);
    }

    public static List<String> readData(String path) {
        try {
            return Files.readAllLines(Paths.get(rootDirectory+path));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void write(String path) {
        try {
            if (!Files.exists(Paths.get(rootDirectory+path))) {
                Files.createFile(Paths.get(rootDirectory+path));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void write(String path, List<String> data) {
        try {
            Files.write(Paths.get(rootDirectory+path), data);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void delete(String path) {
        try {
            Files.delete(Paths.get(rootDirectory+path));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
