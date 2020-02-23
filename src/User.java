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

    public void setUserName(String userName) { data.put("username", userName); }

    public String getPassword() {
        return data.get("password").toString();
    }

    public void setPassword(String password) { data.put("password", password); }
}
