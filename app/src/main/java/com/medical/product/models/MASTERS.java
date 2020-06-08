
package com.medical.product.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MASTERS {

    @SerializedName("OFFER")
    @Expose
    private List<OFFER> oFFER = null;
    @SerializedName("POP")
    @Expose
    private Object pOP;
    @SerializedName("PROFILE")
    @Expose
    private List<PROFILE> pROFILE = null;
    @SerializedName("TESTS")
    @Expose
    private List<TESTS> tESTS = null;

    public List<OFFER> getOFFER() {
        return oFFER;
    }

    public void setOFFER(List<OFFER> oFFER) {
        this.oFFER = oFFER;
    }

    public Object getPOP() {
        return pOP;
    }

    public void setPOP(Object pOP) {
        this.pOP = pOP;
    }

    public List<PROFILE> getPROFILE() {
        return pROFILE;
    }

    public void setPROFILE(List<PROFILE> pROFILE) {
        this.pROFILE = pROFILE;
    }

    public List<TESTS> getTESTS() {
        return tESTS;
    }

    public void setTESTS(List<TESTS> tESTS) {
        this.tESTS = tESTS;
    }

    @Override
    public String toString()
    {
        return "Master [POP = "+pOP+", TESTS = "+tESTS+", PROFILE = "+pROFILE+", OFFER = "+oFFER+"]";
    }
}
