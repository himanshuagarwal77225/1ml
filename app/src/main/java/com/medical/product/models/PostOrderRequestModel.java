package com.medical.product.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PostOrderRequestModel {

    @SerializedName("api_key")
    @Expose
    private String apiKey;
    @SerializedName("orderid")
    @Expose
    private String orderid;
    @SerializedName("address")
    @Expose
    private String address;
    @SerializedName("pincode")
    @Expose
    private String pincode;
    @SerializedName("product")
    @Expose
    private String product;
    @SerializedName("mobile")
    @Expose
    private String mobile;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("service_type")
    @Expose
    private String serviceType;
    @SerializedName("order_by")
    @Expose
    private String orderBy;
    @SerializedName("rate")
    @Expose
    private String rate;
    @SerializedName("hc")
    @Expose
    private String hc;
    @SerializedName("appt_date")
    @Expose
    private String apptDate;
    @SerializedName("reports")
    @Expose
    private String reports;
    @SerializedName("ref_code")
    @Expose
    private String refCode;
    @SerializedName("pay_type")
    @Expose
    private String payType;
    @SerializedName("bencount")
    @Expose
    private String bencount;
    @SerializedName("bendataxml")
    @Expose
    private String bendataxml;

    public PostOrderRequestModel(String apiKey, String orderid, String address, String pincode, String product, String mobile, String email, String serviceType, String orderBy, String rate, String hc, String apptDate, String reports, String refCode, String payType, String bencount, String bendataxml) {
        this.apiKey = apiKey;
        this.orderid = orderid;
        this.address = address;
        this.pincode = pincode;
        this.product = product;
        this.mobile = mobile;
        this.email = email;
        this.serviceType = serviceType;
        this.orderBy = orderBy;
        this.rate = rate;
        this.hc = hc;
        this.apptDate = apptDate;
        this.reports = reports;
        this.refCode = refCode;
        this.payType = payType;
        this.bencount = bencount;
        this.bendataxml = bendataxml;
    }

    public String getApiKey() {
        return apiKey;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    public String getOrderid() {
        return orderid;
    }

    public void setOrderid(String orderid) {
        this.orderid = orderid;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPincode() {
        return pincode;
    }

    public void setPincode(String pincode) {
        this.pincode = pincode;
    }

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }


    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getServiceType() {
        return serviceType;
    }

    public void setServiceType(String serviceType) {
        this.serviceType = serviceType;
    }

    public String getOrderBy() {
        return orderBy;
    }

    public void setOrderBy(String orderBy) {
        this.orderBy = orderBy;
    }

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }

    public String getHc() {
        return hc;
    }

    public void setHc(String hc) {
        this.hc = hc;
    }

    public String getApptDate() {
        return apptDate;
    }

    public void setApptDate(String apptDate) {
        this.apptDate = apptDate;
    }

    public String getReports() {
        return reports;
    }

    public void setReports(String reports) {
        this.reports = reports;
    }

    public String getRefCode() {
        return refCode;
    }

    public void setRefCode(String refCode) {
        this.refCode = refCode;
    }

    public String getPayType() {
        return payType;
    }

    public void setPayType(String payType) {
        this.payType = payType;
    }

    public String getBencount() {
        return bencount;
    }

    public void setBencount(String bencount) {
        this.bencount = bencount;
    }

    @Override
    public String toString() {
        return "PostOrderRequestModel{" +
                "apiKey='" + apiKey + '\'' +
                ", orderid='" + orderid + '\'' +
                ", address='" + address + '\'' +
                ", pincode='" + pincode + '\'' +
                ", product='" + product + '\'' +
                ", mobile='" + mobile + '\'' +
                ", email='" + email + '\'' +
                ", serviceType='" + serviceType + '\'' +
                ", orderBy='" + orderBy + '\'' +
                ", rate='" + rate + '\'' +
                ", hc='" + hc + '\'' +
                ", apptDate='" + apptDate + '\'' +
                ", reports='" + reports + '\'' +
                ", refCode='" + refCode + '\'' +
                ", payType='" + payType + '\'' +
                ", bencount='" + bencount + '\'' +
                ", bendataxml='" + bendataxml + '\'' +
                '}';
    }

    public String getBendataxml() {
        return bendataxml;
    }

    public void setBendataxml(String bendataxml) {
        this.bendataxml = bendataxml;
    }



}