package com.guy.inventory;
import java.util.Date;

public class Buy {
    private Date buyDate, payDate;
    private double price, weight, doneWeight, sum, workDepreciation, wage;
    private int days;
    private String id, supplierName;
    private boolean polish, done, paid;

    private Date created, updated;
    private String objectId, userEmail;

    public double getPrice() {
        return price;
    }

    void setPrice(double price) {
        this.price = price;
    }

    public double getWeight() {
        return weight;
    }

    void setWeight(double weight) {
        this.weight = weight;
    }

    double getDoneWeight() {
        return doneWeight;
    }

    void setDoneWeight(double doneWeight) {
        this.doneWeight = doneWeight;
    }

    double getSum() {
        return sum;
    }

    void setSum(double sum) {
        this.sum = sum;
    }

    double getWorkDepreciation() {
        return workDepreciation;
    }

    void setWorkDepreciation(double workDepreciation) {
        this.workDepreciation = workDepreciation;
    }

    public int getDays() {
        return days;
    }

    void setDays(int days) {
        this.days = days;
    }

    double getWage() {
        return wage;
    }

    void setWage(double wage) {
        this.wage = wage;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    Date getBuyDate() {
        return buyDate;
    }

    void setBuyDate(Date buyDate) {
        this.buyDate = buyDate;
    }

    Date getPayDate() {
        return payDate;
    }

    void setPayDate(Date payDate) {
        this.payDate = payDate;
    }

    boolean isPolish() {
        return polish;
    }

    void setPolish(boolean polish) {
        this.polish = polish;
    }

    boolean isDone() {
        return done;
    }

    void setDone(boolean done) {
        this.done = done;
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

    public boolean isPaid() {
        return paid;
    }

    void setPaid(boolean paid) {
        this.paid = paid;
    }

    String getSupplierName() {
        return supplierName;
    }

    void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
    }
}
