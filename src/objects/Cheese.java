package objects;

import annotations.Unsigned;
import database.DatabaseObject;

public class Cheese extends DatabaseObject {

    private String name;
    @Unsigned
    private float weightGrams;
    @Unsigned
    private float price;

    public Cheese(String name, float weightGrams, float price) {
        this.name = name;
        this.weightGrams = weightGrams;
        this.price = price;
    }

    public Cheese(String id) {
        super(id);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getWeightGrams() {
        return weightGrams;
    }

    public void setWeightGrams(float weightGrams) {
        this.weightGrams = weightGrams;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }
}
