package database;

import java.util.UUID;

public class DatabaseObject {
    protected String id;

    public DatabaseObject() {
        this.id = String.valueOf(UUID.randomUUID());
    }

    public DatabaseObject(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }
}
