package com.medical.product.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PostOrderDataResponse {

    @SerializedName("AGE")
    @Expose
    private String aGE;
    @SerializedName("GENDER")
    @Expose
    private String gENDER;
    @SerializedName("LEAD_ID")
    @Expose
    private String lEADID;
    @SerializedName("NAME")
    @Expose
    private String nAME;

    public String getAGE() {
        return aGE;
    }

    public void setAGE(String aGE) {
        this.aGE = aGE;
    }

    public String getGENDER() {
        return gENDER;
    }

    public void setGENDER(String gENDER) {
        this.gENDER = gENDER;
    }

    public String getLEADID() {
        return lEADID;
    }

    public void setLEADID(String lEADID) {
        this.lEADID = lEADID;
    }

    public String getNAME() {
        return nAME;
    }

    public void setNAME(String nAME) {
        this.nAME = nAME;
    }

}