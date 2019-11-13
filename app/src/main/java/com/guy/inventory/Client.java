package com.guy.inventory;

import java.util.Date;

public class Client {
    private String name, location, phoneNumber, insidePhone, fax, website, details, supplier;

    private Date created, updated;
    private String objectId, userEmail;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    String getLocation() {
        return location;
    }

    void setLocation(String location) {
        this.location = location;
    }

    String getPhoneNumber() {
        return phoneNumber;
    }

    void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    String getInsidePhone() {
        return insidePhone;
    }

    void setInsidePhone(String insidePhone) {
        this.insidePhone = insidePhone;
    }

    String getFax() {
        return fax;
    }

    void setFax(String fax) {
        this.fax = fax;
    }

    String getWebsite() {
        return website;
    }

    void setWebsite(String website) {
        this.website = website;
    }

    String getDetails() {
        return details;
    }

    void setDetails(String details) {
        this.details = details;
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

    public String getSupplier() {
        return supplier;
    }

    void setSupplier(String supplier) {
        this.supplier = supplier;
    }
}
