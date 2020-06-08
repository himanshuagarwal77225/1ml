package com.medical.product.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DocsResponse {

    @SerializedName("utm_source")
    @Expose
    private String utmSource;
    @SerializedName("params")
    @Expose
    private String params;
    @SerializedName("sso")
    @Expose
    private String sso;

    public String getUtmSource() {
        return utmSource;
    }

    public void setUtmSource(String utmSource) {
        this.utmSource = utmSource;
    }

    public String getParams() {
        return params;
    }

    public void setParams(String params) {
        this.params = params;
    }

    public String getSso() {
        return sso;
    }

    public void setSso(String sso) {
        this.sso = sso;
    }

}