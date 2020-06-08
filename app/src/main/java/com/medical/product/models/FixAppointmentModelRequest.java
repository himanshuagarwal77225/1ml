package com.medical.product.models;

public class FixAppointmentModelRequest {

    private String API_key;
    private String VisitId;
    private String Pincode;
    private String AppointmentDate;

    public FixAppointmentModelRequest(String API_key, String visitId, String pincode, String appointmentDate) {
        this.API_key = API_key;
        VisitId = visitId;
        Pincode = pincode;
        AppointmentDate = appointmentDate;
    }

    public String getAPI_key() {
        return API_key;
    }

    public void setAPI_key(String API_key) {
        this.API_key = API_key;
    }

    public String getVisitId() {
        return VisitId;
    }

    public void setVisitId(String visitId) {
        VisitId = visitId;
    }

    public String getPincode() {
        return Pincode;
    }

    public void setPincode(String pincode) {
        Pincode = pincode;
    }

    public String getAppointmentDate() {
        return AppointmentDate;
    }

    public void setAppointmentDate(String appointmentDate) {
        AppointmentDate = appointmentDate;
    }
}
