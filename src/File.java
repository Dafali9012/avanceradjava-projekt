import java.nio.file.Path;
import java.util.List;

public class File {
    String path;
    List<String> data;

    public File(String path) {
        this.path = path;
        data = DatabaseManager.readData(path);
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public List<String> getData() {
        return data;
    }

    public void setData(List<String> data) {
        this.data = data;
    }
}
