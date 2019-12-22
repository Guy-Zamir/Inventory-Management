package com.guy.inventory.Tables;

import com.backendless.Backendless;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.persistence.*;

import java.util.List;
import java.util.Date;

public class SortInfo
{
    private Date updated;
    private boolean fromBuy;
    private boolean fromSale;
    private Integer sortCount;
    private Double price;
    private String name;
    private Double weight;
    private Double sum;
    private String objectId;
    private String fromId;
    private String toId;
    private String ownerId;
    private Date created;

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

    public String getName()
    {
        return name;
    }

    public void setName( String name )
    {
        this.name = name;
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

    public boolean isFromBuy() {
        return fromBuy;
    }

    public void setFromBuy(boolean fromBuy) {
        this.fromBuy = fromBuy;
    }

    public boolean isFromSale() {
        return fromSale;
    }

    public void setFromSale(boolean fromSale) {
        this.fromSale = fromSale;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    public String getFromId() {
        return fromId;
    }

    public void setFromId(String fromId) {
        this.fromId = fromId;
    }

    public String getToId() {
        return toId;
    }

    public void setToId(String toId) {
        this.toId = toId;
    }
}