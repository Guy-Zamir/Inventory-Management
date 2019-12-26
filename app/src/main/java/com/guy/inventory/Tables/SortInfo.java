package com.guy.inventory.Tables;

import com.backendless.Backendless;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.persistence.*;

import java.util.List;
import java.util.Date;

public class SortInfo
{
    private Date updated;
    private Integer sortCount;
    private Double price;
    private String fromName;
    private String toName;
    private String fromId;
    private Double weight;
    private Double sum;
    private String objectId;
    private String toId;
    private String ownerId;
    private Date created;
    private String userEmail;
    private boolean sale;
    private boolean buy;
    private boolean split;
    private double soldPrice;

    public Date getUpdated()
    {
        return updated;
    }

    public Integer getSortCount()
    {
        return sortCount;
    }

    public void setSortCount( Integer sortCount )
    {
        this.sortCount = sortCount;
    }

    public Double getPrice()
    {
        return price;
    }

    public void setPrice( Double price )
    {
        this.price = price;
    }

    public String getFromName()
    {
        return fromName;
    }

    public void setFromName(String fromName )
    {
        this.fromName = fromName;
    }

    public Double getWeight()
    {
        return weight;
    }

    public void setWeight( Double weight )
    {
        this.weight = weight;
    }

    public Double getSum()
    {
        return sum;
    }

    public void setSum( Double sum )
    {
        this.sum = sum;
    }

    public String getObjectId()
    {
        return objectId;
    }

    public String getOwnerId()
    {
        return ownerId;
    }

    public Date getCreated()
    {
        return created;
    }


    public SortInfo save()
    {
        return Backendless.Data.of( SortInfo.class ).save( this );
    }

    public void saveAsync( AsyncCallback<SortInfo> callback )
    {
        Backendless.Data.of( SortInfo.class ).save( this, callback );
    }

    public Long remove()
    {
        return Backendless.Data.of( SortInfo.class ).remove( this );
    }

    public void removeAsync( AsyncCallback<Long> callback )
    {
        Backendless.Data.of( SortInfo.class ).remove( this, callback );
    }

    public static SortInfo findById(String id )
    {
        return Backendless.Data.of( SortInfo.class ).findById( id );
    }

    public static void findByIdAsync( String id, AsyncCallback<SortInfo> callback )
    {
        Backendless.Data.of( SortInfo.class ).findById( id, callback );
    }

    public static SortInfo findFirst()
    {
        return Backendless.Data.of( SortInfo.class ).findFirst();
    }

    public static void findFirstAsync( AsyncCallback<SortInfo> callback )
    {
        Backendless.Data.of( SortInfo.class ).findFirst( callback );
    }

    public static SortInfo findLast()
    {
        return Backendless.Data.of( SortInfo.class ).findLast();
    }

    public static void findLastAsync( AsyncCallback<SortInfo> callback )
    {
        Backendless.Data.of( SortInfo.class ).findLast( callback );
    }

    public static List<SortInfo> find(DataQueryBuilder queryBuilder )
    {
        return Backendless.Data.of( SortInfo.class ).find( queryBuilder );
    }

    public static void findAsync( DataQueryBuilder queryBuilder, AsyncCallback<List<SortInfo>> callback )
    {
        Backendless.Data.of( SortInfo.class ).find( queryBuilder, callback );
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    public String getToId() {
        return toId;
    }

    public void setToId(String toId) {
        this.toId = toId;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getFromId() {
        return fromId;
    }

    public void setFromId(String fromId) {
        this.fromId = fromId;
    }

    public String getToName() {
        return toName;
    }

    public void setToName(String toName) {
        this.toName = toName;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public boolean isSale() {
        return sale;
    }

    public void setSale(boolean sale) {
        this.sale = sale;
    }

    public boolean isBuy() {
        return buy;
    }

    public void setBuy(boolean buy) {
        this.buy = buy;
    }

    public double getSoldPrice() {
        return soldPrice;
    }

    public void setSoldPrice(double soldPrice) {
        this.soldPrice = soldPrice;
    }

    public boolean isSplit() {
        return split;
    }

    public void setSplit(boolean split) {
        this.split = split;
    }
}