package com.medical.product.models;

public class AddModel {

    public String flat;
    public String address;

    public AddModel() {
    }

    public AddModel(String flat, String address, String state, String city, String pincode) {
        this.flat = flat;
        this.address = address;
        this.state = state;
        this.city = city;
        this.pincode = pincode;
    }

    public String getFlat() {
        return flat;
    }

    public void setFlat(String flat) {
        this.flat = flat;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getPincode() {
        return pincode;
    }

    public void setPincode(String pincode) {
        this.pincode = pincode;
    }

    public String state;
    public String city;
    public String pincode;

}
