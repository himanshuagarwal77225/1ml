package com.medical.product.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class FixAppointmentModelResponse {
    @SerializedName("RES_ID")
    @Expose
    private String RES_ID;
    @SerializedName("RESPONSE")
    @Expose
    private String RESPONSE;

    public String getRES_ID ()
    {
        return RES_ID;
    }

    public void setRES_ID (String RES_ID)
    {
        this.RES_ID = RES_ID;
    }

    public String getRESPONSE ()
    {
        return RESPONSE;
    }

    public void setRESPONSE (String RESPONSE)
    {
        this.RESPONSE = RESPONSE;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [RES_ID = "+RES_ID+", RESPONSE = "+RESPONSE+"]";
    }
}
