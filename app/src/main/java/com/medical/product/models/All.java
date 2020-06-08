
package com.medical.product.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class All {

    @SerializedName("B2B_MASTERS")
    @Expose
    private Object b2BMASTERS;
    @SerializedName("MASTERS")
    @Expose
    private MASTERS mASTERS;
    @SerializedName("RESPONSE")
    @Expose
    private String rESPONSE;
    @SerializedName("RES_ID")
    @Expose
    private String rESID;
    @SerializedName("USER_TYPE")
    @Expose
    private String uSERTYPE;


    public Object getB2BMASTERS() {
        return b2BMASTERS;
    }

    public void setB2BMASTERS(Object b2BMASTERS) {
        this.b2BMASTERS = b2BMASTERS;
    }

    public MASTERS getMASTERS() {
        return mASTERS;
    }

    public void setMASTERS(MASTERS mASTERS) {
        this.mASTERS = mASTERS;
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

    public String getUSERTYPE() {
        return uSERTYPE;
    }

    public void setUSERTYPE(String uSERTYPE) {
        this.uSERTYPE = uSERTYPE;
    }

    @Override
    public String toString()
    {
        return "All [B2B_MASTERS = "+b2BMASTERS+", MASTERS = "+mASTERS+", USER_TYPE = "+uSERTYPE+", RES_ID = "+rESID+", RESPONSE = "+rESPONSE+"]";
    }

}
