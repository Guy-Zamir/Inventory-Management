package com.guy.inventory;
import java.util.Date;

public class Sale {
    private double saleSum, weight, price;
    private String id, clientName;
    private int days;
    private boolean paid, polish;
    private Date saleDate, payDate;

    private Date created, updated;
    private String objectId, userEmail;

    double getSaleSum() {
        return saleSum;
    }

    void setSaleSum(double saleSum) {
        this.saleSum = saleSum;
    }

    public double getWeight() {
        return weight;
    }

    void setWeight(double weight) {
        this.weight = weight;
    }

    public double getPrice() {
        return price;
    }

    void setPrice(double price) {
        this.price = price;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getDays() {
        return days;
    }

    void setDays(int days) {
        this.days = days;
    }

    public boolean isPaid() {
        return paid;
    }

    void setPaid(boolean paid) {
        this.paid = paid;
    }

    Date getSaleDate() {
        return saleDate;
    }

    void setSaleDate(Date saleDate) {
        this.saleDate = saleDate;
    }

    Date getPayDate() {
        return payDate;
    }

    void setPayDate(Date payDate) {
        this.payDate = payDate;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public Date getUpdated() {
        return updated;
    }

    public void setUpdated(Date updated) {
        this.updated = updated;
    }

    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    String getClientName() {
        return clientName;
    }

    void setClientName(String clientName) {
        this.clientName = clientName;
    }

    boolean isPolish() {
        return polish;
    }

    void setPolish(boolean polish) {
        this.polish = polish;
    }
}
