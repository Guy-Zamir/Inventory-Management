package com.guy.inventory.Tables;

import com.backendless.Backendless;
import com.backendless.BackendlessUser;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.persistence.*;
import com.backendless.geo.GeoPoint;

import java.util.List;
import java.util.Date;

public class Sort
{
    private String color;
    private Date updated;
    private String size;
    private String clarity;
    private String objectId;
    private Double sum;
    private Double weight;
    private String ownerId;
    private String shape;
    private Boolean last;
    private Integer sortCount;
    private String userEmail;
    private String name;
    private Double soldPrice;
    private Double price;
    private Boolean sale;
    private Date created;

    public String getColor()
    {
        return color;
    }

    public void setColor( String color )
    {
        this.color = color;
    }

    public Date getUpdated()
    {
        return updated;
    }

    public String getSize()
    {
        return size;
    }

    public void setSize( String size )
    {
        this.size = size;
    }

    public String getClarity()
    {
        return clarity;
    }

    public void setClarity( String clarity )
    {
        this.clarity = clarity;
    }

    public String getObjectId()
    {
        return objectId;
    }

    public Double getSum()
    {
        return sum;
    }

    public void setSum( Double sum )
    {
        this.sum = sum;
    }

    public Double getWeight()
    {
        return weight;
    }

    public void setWeight( Double weight )
    {
        this.weight = weight;
    }

    public String getOwnerId()
    {
        return ownerId;
    }

    public String getShape()
    {
        return shape;
    }

    public void setShape( String shape )
    {
        this.shape = shape;
    }

    public Boolean getLast()
    {
        return last;
    }

    public void setLast( Boolean last )
    {
        this.last = last;
    }

    public Integer getSortCount()
    {
        return sortCount;
    }

    public void setSortCount( Integer sortCount )
    {
        this.sortCount = sortCount;
    }

    public String getUserEmail()
    {
        return userEmail;
    }

    public void setUserEmail( String userEmail )
    {
        this.userEmail = userEmail;
    }

    public String getName()
    {
        return name;
    }

    public void setName( String name )
    {
        this.name = name;
    }

    public Double getSoldPrice()
    {
        return soldPrice;
    }

    public void setSoldPrice( Double soldPrice )
    {
        this.soldPrice = soldPrice;
    }

    public Double getPrice()
    {
        return price;
    }

    public void setPrice( Double price )
    {
        this.price = price;
    }

    public Boolean getSale()
    {
        return sale;
    }

    public void setSale( Boolean sale )
    {
        this.sale = sale;
    }

    public Date getCreated()
    {
        return created;
    }


    public Sort save()
    {
        return Backendless.Data.of( Sort.class ).save( this );
    }

    public void saveAsync( AsyncCallback<Sort> callback )
    {
        Backendless.Data.of( Sort.class ).save( this, callback );
    }

    public Long remove()
    {
        return Backendless.Data.of( Sort.class ).remove( this );
    }

    public void removeAsync( AsyncCallback<Long> callback )
    {
        Backendless.Data.of( Sort.class ).remove( this, callback );
    }

    public static Sort findById( String id )
    {
        return Backendless.Data.of( Sort.class ).findById( id );
    }

    public static void findByIdAsync( String id, AsyncCallback<Sort> callback )
    {
        Backendless.Data.of( Sort.class ).findById( id, callback );
    }

    public static Sort findFirst()
    {
        return Backendless.Data.of( Sort.class ).findFirst();
    }

    public static void findFirstAsync( AsyncCallback<Sort> callback )
    {
        Backendless.Data.of( Sort.class ).findFirst( callback );
    }

    public static Sort findLast()
    {
        return Backendless.Data.of( Sort.class ).findLast();
    }

    public static void findLastAsync( AsyncCallback<Sort> callback )
    {
        Backendless.Data.of( Sort.class ).findLast( callback );
    }

    public static List<Sort> find( DataQueryBuilder queryBuilder )
    {
        return Backendless.Data.of( Sort.class ).find( queryBuilder );
    }

    public static void findAsync( DataQueryBuilder queryBuilder, AsyncCallback<List<Sort>> callback )
    {
        Backendless.Data.of( Sort.class ).find( queryBuilder, callback );
    }
}