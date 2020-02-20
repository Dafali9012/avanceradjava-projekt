public class User extends DatabaseObject {

    public User(String username, String password) {
        super("users");
        data.put("username", username);
        data.put("password", password);
    }

    public String getUserName() {
        return data.get("username").toString();
    }

    public String getPassword() {
        return data.get("password").toString();
    }
}
