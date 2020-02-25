package objects;

import annotations.Unsigned;
import database.DatabaseObject;

public class Wine extends DatabaseObject {
    private String name;
    @Unsigned
    private float amountML;
    @Unsigned
    private float proof;
    @Unsigned
    private float price;

    public Wine(String name, float amountML, float proof, float price) {
        this.name = name;
        this.amountML = amountML;
        this.proof = proof;
        this.price = price;
    }

    public Wine(String id) {
        super(id);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getAmountML() {
        return amountML;
    }

    public void setAmountML(float amountML) {
        this.amountML = amountML;
    }

    public float getProof() {
        return proof;
    }

    public void setProof(float proof) {
        this.proof = proof;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }
}
