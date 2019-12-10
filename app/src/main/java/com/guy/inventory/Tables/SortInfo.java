package com.guy.inventory.Tables;

public class SortInfo {

    private double weight;
    private double price;
    private double sum;
    private String referenceId;
    private boolean fromSale;

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getSum() {
        return sum;
    }

    public void setSum(double sum) {
        this.sum = sum;
    }

    public String getReferenceId() {
        return referenceId;
    }

    public boolean isFromSale() {
        return fromSale;
    }
}
