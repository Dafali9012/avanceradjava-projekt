package database;

import java.util.Date;

public class DatabaseObject {
    protected String id;

    public DatabaseObject() {
        this.id = String.valueOf(new Date().getTime());
    }

    public DatabaseObject(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }
}
