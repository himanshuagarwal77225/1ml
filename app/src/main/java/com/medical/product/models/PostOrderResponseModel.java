package com.medical.product.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PostOrderResponseModel {
    @SerializedName("ADDRESS")
    @Expose
    private String aDDRESS;
    @SerializedName("BOOKED_BY")
    @Expose
    private String bOOKEDBY;
    @SerializedName("CUSTOMER_RATE")
    @Expose
    private Integer cUSTOMERRATE;
    @SerializedName("EMAIL")
    @Expose
    private String eMAIL;
    @SerializedName("FASTING")
    @Expose
    private String fASTING;
    @SerializedName("MOBILE")
    @Expose
    private String mOBILE;
    @SerializedName("MODE")
    @Expose
    private String mODE;
    @SerializedName("ORDERRESPONSE")
    @Expose
    private ORDERRESPONSE oRDERRESPONSE;
    @SerializedName("ORDER_NO")
    @Expose
    private String oRDERNO;
    @SerializedName("PAY_TYPE")
    @Expose
    private String pAYTYPE;
    @SerializedName("PRODUCT")
    @Expose
    private String pRODUCT;
    @SerializedName("REF_ORDERID")
    @Expose
    private String rEFORDERID;
    @SerializedName("REPORT_HARD_COPY")
    @Expose
    private String rEPORTHARDCOPY;
    @SerializedName("RESPONSE")
    @Expose
    private String rESPONSE;
    @SerializedName("RES_ID")
    @Expose
    private String rESID;
    @SerializedName("SERVICE_TYPE")
    @Expose
    private String sERVICETYPE;
    @SerializedName("STATUS")
    @Expose
    private String sTATUS;

    public String getADDRESS() {
        return aDDRESS;
    }

    public void setADDRESS(String aDDRESS) {
        this.aDDRESS = aDDRESS;
    }

    public String getBOOKEDBY() {
        return bOOKEDBY;
    }

    public void setBOOKEDBY(String bOOKEDBY) {
        this.bOOKEDBY = bOOKEDBY;
    }

    public Integer getCUSTOMERRATE() {
        return cUSTOMERRATE;
    }

    public void setCUSTOMERRATE(Integer cUSTOMERRATE) {
        this.cUSTOMERRATE = cUSTOMERRATE;
    }

    public String getEMAIL() {
        return eMAIL;
    }

    public void setEMAIL(String eMAIL) {
        this.eMAIL = eMAIL;
    }

    public String getFASTING() {
        return fASTING;
    }

    public void setFASTING(String fASTING) {
        this.fASTING = fASTING;
    }

    public String getMOBILE() {
        return mOBILE;
    }

    public void setMOBILE(String mOBILE) {
        this.mOBILE = mOBILE;
    }

    public String getMODE() {
        return mODE;
    }

    public void setMODE(String mODE) {
        this.mODE = mODE;
    }

    public ORDERRESPONSE getORDERRESPONSE() {
        return oRDERRESPONSE;
    }

    public void setORDERRESPONSE(ORDERRESPONSE oRDERRESPONSE) {
        this.oRDERRESPONSE = oRDERRESPONSE;
    }

    public String getORDERNO() {
        return oRDERNO;
    }

    public void setORDERNO(String oRDERNO) {
        this.oRDERNO = oRDERNO;
    }

    public String getPAYTYPE() {
        return pAYTYPE;
    }

    public void setPAYTYPE(String pAYTYPE) {
        this.pAYTYPE = pAYTYPE;
    }

    public String getPRODUCT() {
        return pRODUCT;
    }

    public void setPRODUCT(String pRODUCT) {
        this.pRODUCT = pRODUCT;
    }

    public String getREFORDERID() {
        return rEFORDERID;
    }

    public void setREFORDERID(String rEFORDERID) {
        this.rEFORDERID = rEFORDERID;
    }

    public String getREPORTHARDCOPY() {
        return rEPORTHARDCOPY;
    }

    public void setREPORTHARDCOPY(String rEPORTHARDCOPY) {
        this.rEPORTHARDCOPY = rEPORTHARDCOPY;
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

    public String getSERVICETYPE() {
        return sERVICETYPE;
    }

    public void setSERVICETYPE(String sERVICETYPE) {
        this.sERVICETYPE = sERVICETYPE;
    }

    public String getSTATUS() {
        return sTATUS;
    }

    public void setSTATUS(String sTATUS) {
        this.sTATUS = sTATUS;
    }

}