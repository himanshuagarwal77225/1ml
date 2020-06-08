package com.medical.product.models;

public class GatterGetFamilyMamberListModel {

    private String id,member_name,contact_number,realtion;

    public GatterGetFamilyMamberListModel(String id, String member_name, String contact_numbe,String realtion) {
        this.id = id;
        this.member_name = member_name;
        this.contact_number = contact_number;
        this.realtion=realtion;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMember_name() {
        return member_name;
    }

    public void setMember_name(String member_name) {
        this.member_name = member_name;
    }

    public String getContact_number() {
        return contact_number;
    }

    public void setContact_number(String contact_number) {
        this.contact_number = contact_number;
    }

    public String getRealtion() {
        return realtion;
    }

    public void setRealtion(String realtion) {
        this.realtion = realtion;
    }
}
