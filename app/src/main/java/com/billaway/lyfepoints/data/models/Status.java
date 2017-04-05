package com.billaway.lyfepoints.data.models;


import com.google.gson.annotations.SerializedName;

public class Status {
    @SerializedName("status")
    private boolean success;
    private String message;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }
}
