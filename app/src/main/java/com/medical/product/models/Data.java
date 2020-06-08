package com.medical.product.models;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Data {

    @SerializedName("subject")
    @Expose
    private String subject;
    @SerializedName("fromAddress")
    @Expose
    private String fromAddress;
    @SerializedName("toAddress")
    @Expose
    private String toAddress;
    @SerializedName("content")
    @Expose
    private String content;
    @SerializedName("ccAddress")
    @Expose
    private String ccAddress;

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getFromAddress() {
        return fromAddress;
    }

    public void setFromAddress(String fromAddress) {
        this.fromAddress = fromAddress;
    }

    public String getToAddress() {
        return toAddress;
    }

    public void setToAddress(String toAddress) {
        this.toAddress = toAddress;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCcAddress() {
        return ccAddress;
    }

    public void setCcAddress(String ccAddress) {
        this.ccAddress = ccAddress;
    }

}
