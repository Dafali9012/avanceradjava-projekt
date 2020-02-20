import java.util.Date;
import java.util.LinkedHashMap;

public abstract class DatabaseObject {
    protected LinkedHashMap data = new LinkedHashMap();

    public DatabaseObject(String subFolder) {
        data.put("subfolder", subFolder);
        data.put("id", String.valueOf(new Date().getTime()));
    }

    public String getSubFolder() {
        return data.get("subfolder").toString();
    }

    public String getId() {
        return data.get("id").toString();
    }

    public LinkedHashMap<String, String> getData() {
        return data;
    }
}
