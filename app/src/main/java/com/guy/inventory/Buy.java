package com.guy.inventory;

public class Buy {
    private double price, weight, doneWeight, wage;
    private String supplier, id, buyDate;
    private boolean polish, done;

    public Buy() {
        this.buyDate = null;
        this.supplier = null;
        this.id = null;
        this.price  = 0;
        this.weight = 0;
        this.polish = false;
        this.done = false;
        this.doneWeight = 0;
        this.wage = 0;
    }

    public double getPrice() {
        return price;
    }

    public double getWeight() {
        return weight;
    }

    public double getDoneWeight() {
        return doneWeight;
    }

    public double getWage() {
        return wage;
    }

    public String getSupplier() {
        return supplier;
    }

    public String getId() {
        return id;
    }

    public String getBuyDate() {
        return buyDate;
    }

    public boolean isPolish() {
        return polish;
    }

    public boolean isDone() {
        return done;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public void setDoneWeight(double doneWeight) {
        this.doneWeight = doneWeight;
    }

    public void setWage(double wage) {
        this.wage = wage;
    }

    public void setSupplier(String supplier) {
        this.supplier = supplier;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setBuyDate(String buyDate) {
        this.buyDate = buyDate;
    }

    public void setPolish(boolean polish) {
        this.polish = polish;
    }

    public void setDone(boolean done) {
        this.done = done;
    }
}
