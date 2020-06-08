
package com.medical.product.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Rate {

    @SerializedName("b2b")
    @Expose
    private String b2b;
    @SerializedName("b2c")
    @Expose
    private String b2c;
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("offer_rate")
    @Expose
    private String offerRate;
    @SerializedName("pay_amt")
    @Expose
    private String payAmt;


    public String getB2b() {
        return b2b;
    }

    public void setB2b(String b2b) {
        this.b2b = b2b;
    }

    public String getB2c() {
        return b2c;
    }

    public void setB2c(String b2c) {
        this.b2c = b2c;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOfferRate() {
        return offerRate;
    }

    public void setOfferRate(String offerRate) {
        this.offerRate = offerRate;
    }

    public String getPayAmt() {
        return payAmt;
    }

    public void setPayAmt(String payAmt) {
        this.payAmt = payAmt;
    }

    @Override
    public String toString()
    {
        return "Rate [offer_rate = "+offerRate+", b2c = "+b2c+", b2b = "+b2b+", pay_amt = "+payAmt+", id = "+id+"]";
    }


}
