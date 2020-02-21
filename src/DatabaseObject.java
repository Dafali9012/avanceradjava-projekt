import java.util.Date;
import java.util.LinkedHashMap;

public class DatabaseObject {
    protected LinkedHashMap data = new LinkedHashMap<>();
    protected String id;

    public DatabaseObject() {
        this.id = String.valueOf(new Date().getTime());
    }

    public DatabaseObject(LinkedHashMap<String, String> data, String id) {
        this.data = data;
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public LinkedHashMap<String, String> getData() {
        return data;
    }
}
