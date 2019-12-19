package com.guy.inventory.Tables;
import com.backendless.Backendless;
import com.backendless.BackendlessUser;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.persistence.*;
import com.backendless.geo.GeoPoint;
import java.util.Date;
import java.util.List;

public class Sale {
    private Client client;

    private double saleSum, weight, price;
    private String id, clientName;
    private int days;
    private boolean paid, polish;
    private Date saleDate, payDate;

    private Date created, updated;
    private String objectId, userEmail, kind;

    public double getSaleSum() {
        return saleSum;
    }

    public void setSaleSum(double saleSum) {
        this.saleSum = saleSum;
    }

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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getDays() {
        return days;
    }

    public void setDays(int days) {
        this.days = days;
    }

    public boolean isPaid() {
        return paid;
    }

    public void setPaid(boolean paid) {
        this.paid = paid;
    }

    public Date getSaleDate() {
        return saleDate;
    }

    public void setSaleDate(Date saleDate) {
        this.saleDate = saleDate;
    }

    public Date getPayDate() {
        return payDate;
    }

    public void setPayDate(Date payDate) {
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

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public boolean isPolish() {
        return polish;
    }

    public void setPolish(boolean polish) {
        this.polish = polish;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
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

    public Sale save()
    {
        return Backendless.Data.of( Sale.class ).save( this );
    }

    public void saveAsync( AsyncCallback<Sale> callback )
    {
        Backendless.Data.of( Sale.class ).save( this, callback );
    }

    public Long remove()
    {
        return Backendless.Data.of( Sale.class ).remove( this );
    }

    public void removeAsync( AsyncCallback<Long> callback )
    {
        Backendless.Data.of( Sale.class ).remove( this, callback );
    }

    public static Sale findById( String id )
    {
        return Backendless.Data.of( Sale.class ).findById( id );
    }

    public static void findByIdAsync( String id, AsyncCallback<Sale> callback )
    {
        Backendless.Data.of( Sale.class ).findById( id, callback );
    }

    public static Sale findFirst()
    {
        return Backendless.Data.of( Sale.class ).findFirst();
    }

    public static void findFirstAsync( AsyncCallback<Sale> callback )
    {
        Backendless.Data.of( Sale.class ).findFirst( callback );
    }

    public static Sale findLast()
    {
        return Backendless.Data.of( Sale.class ).findLast();
    }

    public static void findLastAsync( AsyncCallback<Sale> callback )
    {
        Backendless.Data.of( Sale.class ).findLast( callback );
    }

    public static List<Sale> find(DataQueryBuilder queryBuilder )
    {
        return Backendless.Data.of( Sale.class ).find( queryBuilder );
    }

    public static void findAsync( DataQueryBuilder queryBuilder, AsyncCallback<List<Sale>> callback )
    {
        Backendless.Data.of( Sale.class ).find( queryBuilder, callback );
    }
}
