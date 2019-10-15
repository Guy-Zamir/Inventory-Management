package com.guy.inventory;

public class Sale {
    private double saleSum;
    private int date;
    private String company, id;

    public Sale(int date, String company, String id, double saleSum) {
        this.saleSum = saleSum;
        this.company = company;
        this.id = id;
        this.date = date;
    }

    public double getSaleSum() {
        return saleSum;
    }

    public void setSaleSum(double saleSum) {
        this.saleSum = saleSum;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getDate() {
        return date;
    }

    public void setDate(int date) {
        this.date = date;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }
}
