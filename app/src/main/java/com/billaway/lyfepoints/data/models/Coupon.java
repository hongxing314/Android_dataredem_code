package com.billaway.lyfepoints.data.models;


import java.io.Serializable;
import java.util.Date;

public class Coupon implements Serializable {
    private int id;
    private String baCouponId;
    private String brandImage;
    private float creditAwarded;
    private String brandName;
    private String description;
    private Date endDate;
    private Date startDate;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getBaCouponId() {
        return baCouponId;
    }

    public void setBaCouponId(String baCouponId) {
        this.baCouponId = baCouponId;
    }

    public String getBrandImage() {
        return brandImage;
    }

    public void setBrandImage(String brandImage) {
        this.brandImage = brandImage;
    }


    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public float getCreditAwarded() {
        return creditAwarded;
    }

    public void setCreditAwarded(float creditAwarded) {
        this.creditAwarded = creditAwarded;
    }
}
