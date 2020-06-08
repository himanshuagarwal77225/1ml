package com.medical.product.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Pincode {

    @SerializedName("LastMonthsOrders")
    @Expose
    private String lastMonthsOrders;
    @SerializedName("Res_id")
    @Expose
    private String resId;
    @SerializedName("Response_Pincode")
    @Expose
    private String responsePincode;
    @SerializedName("status")
    @Expose
    private String status;

    public String getLastMonthsOrders() {
        return lastMonthsOrders;
    }

    public void setLastMonthsOrders(String lastMonthsOrders) {
        this.lastMonthsOrders = lastMonthsOrders;
    }

    public String getResId() {
        return resId;
    }

    public void setResId(String resId) {
        this.resId = resId;
    }

    public String getResponsePincode() {
        return responsePincode;
    }

    public void setResponsePincode(String responsePincode) {
        this.responsePincode = responsePincode;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}