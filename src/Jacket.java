import database.DatabaseObject;

import java.time.LocalDate;

public class Jacket extends DatabaseObject {
    public Jacket(float price, String name, LocalDate expirationDate) {
        this.price = price;
        this.name = name;
        this.expirationDate = expirationDate;
    }

    public Jacket(String id) {
        super(id);
    }

    //@NonNegative
    private float price;
    private String name;
    private LocalDate expirationDate;

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDate getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(LocalDate expirationDate) {
        this.expirationDate = expirationDate;
    }
}
