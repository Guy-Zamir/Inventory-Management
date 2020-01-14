package com.guy.inventory.Tables;

import com.backendless.Backendless;

public class Memo {
    private String name;
    private String clientName;
    private String userEmail;
    private String objectId;
    private String fromSortId;
    private String kind;
    private Double price;
    private Double priceINV;
    private Double weight;
    private Double sum;
    private Double sumINV;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    public String getKind() {
        return kind;
    }

    public void setKind(String kind) {
        this.kind = kind;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Double getPriceINV() {
        return priceINV;
    }

    public void setPriceINV(Double priceINV) {
        this.priceINV = priceINV;
    }

    public Double getWeight() {
        return weight;
    }

    public void setWeight(Double weight) {
        this.weight = weight;
    }

    public Double getSum() {
        return sum;
    }

    public void setSum(Double sum) {
        this.sum = sum;
    }

    public Double getSumINV() {
        return sumINV;
    }

    public void setSumINV(Double sumINV) {
        this.sumINV = sumINV;
    }

    public String getFromSortId() {
        return fromSortId;
    }

    public void setFromSortId(String fromSortId) {
        this.fromSortId = fromSortId;
    }

    public Memo save()
    {
        return Backendless.Data.of( Memo.class ).save( this );
    }

    public Long remove()
    {
        return Backendless.Data.of( Memo.class ).remove( this );
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }
}

