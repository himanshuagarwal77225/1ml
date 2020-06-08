package com.medical.product.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Duplicate {
    @SerializedName("REMARKS")
    @Expose
    private String rEMARKS;
    @SerializedName("RESPONSE")
    @Expose
    private String rESPONSE;
    @SerializedName("RES_ID")
    @Expose
    private String rESID;

    public String getREMARKS() {
        return rEMARKS;
    }

    public void setREMARKS(String rEMARKS) {
        this.rEMARKS = rEMARKS;
    }

    public String getRESPONSE() {
        return rESPONSE;
    }

    public void setRESPONSE(String rESPONSE) {
        this.rESPONSE = rESPONSE;
    }

    public String getRESID() {
        return rESID;
    }

    public void setRESID(String rESID) {
        this.rESID = rESID;
    }

}