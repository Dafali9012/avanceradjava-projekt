import java.util.LinkedHashMap;

public class User extends DatabaseObject {

    public User(String username, String password) {
        data.put("username", username);
        data.put("password", password);
    }

    public User(LinkedHashMap<String, String> data, String id) {
        super(data, id);
    }

    public String getUserName() {
        return data.get("username").toString();
    }

    public String getPassword() {
        return data.get("password").toString();
    }
}
