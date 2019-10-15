package com.guy.inventory;

public class Buy {
    private double price, weight, doneWeight, wage;
    private String supplier, id, date;
    private boolean polish, done;

    public Buy(String date, String supplier, String id, double price, double weight, boolean polish, boolean done, double doneWeight, double wage) {
        this.date = date;
        this.supplier = supplier;
        this.id = id;
        this.price = price;
        this.weight = weight;
        this.polish = polish;
        this.done = done;
        this.doneWeight = doneWeight;
        this.wage = wage;
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

    public String getDate() {
        return date;
    }

    public boolean isPolish() {
        return polish;
    }

    public boolean isDone() {
        return done;
    }
}
