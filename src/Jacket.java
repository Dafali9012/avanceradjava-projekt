import Database.DatabaseObject;

public class Jacket extends DatabaseObject {
    public Jacket(String price, String name, String expirationDate) {
        this.price = price;
        this.name = name;
        this.expirationDate = expirationDate;
    }

    public Jacket(String id) {
        super(id);
    }

    private String price;
    private String name;
    private String expirationDate;

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(String expirationDate) {
        this.expirationDate = expirationDate;
    }
}
