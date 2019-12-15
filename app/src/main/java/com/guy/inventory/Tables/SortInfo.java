package com.guy.inventory.Tables;

import java.util.Date;

public class SortInfo {

    private double weight;
    private double price;
    private double sum;
    private String fromId;
    private String toId;
    private boolean fromSale;
    private boolean fromBuy;
    private boolean letOver;
    private String userEmail;
    private Date created;

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

    public boolean isFromSale() {
        return fromSale;
    }

    public String getFromId() {
        return fromId;
    }

    public String getToId() {
        return toId;
    }

    public boolean isFromBuy() {
        return fromBuy;
    }

    public void setFromSale(boolean fromSale) {
        this.fromSale = fromSale;
    }

    public void setFromBuy(boolean fromBuy) {
        this.fromBuy = fromBuy;
    }

    public void setFromId(String fromId) {
        this.fromId = fromId;
    }

    public void setToId(String toId) {
        this.toId = toId;
    }

    public boolean isLetOver() {
        return letOver;
    }

    public void setLetOver(boolean letOver) {
        this.letOver = letOver;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }
}
