package com.medical.product.models;

import java.io.Serializable;

public class
GatterGetCartList implements Serializable{

    String cart_id,user_id,quentity,vendor_name,prdct_id,vendor_id,med_name,sale_price,mrp,additional_offer_percentage,additional_offer_id,additional_offer_code,additional_offer_decription,additional_offer_start,additional_offer_expired,image,company_name,additional_offer_type,additional_offer;

    public GatterGetCartList(String cart_id, String user_id, String quentity, String vendor_name, String prdct_id, String vendor_id, String med_name, String sale_price, String mrp, String additional_offer_percentage, String additional_offer_id, String additional_offer_code,  String image,String company_name,String additional_offer_type,String additional_offer) {
        this.cart_id = cart_id;
        this.user_id = user_id;
        this.quentity = quentity;
        this.vendor_name = vendor_name;
        this.prdct_id = prdct_id;
        this.vendor_id = vendor_id;
        this.med_name = med_name;
        this.sale_price = sale_price;
        this.mrp = mrp;
        this.additional_offer_percentage = additional_offer_percentage;
        this.additional_offer_id = additional_offer_id;
        this.additional_offer_code = additional_offer_code;
        this.image = image;
        this.company_name=company_name;
        this.additional_offer_type=additional_offer_type;
        this.additional_offer=additional_offer;
    }

    public String getCart_id() {
        return cart_id;
    }

    public void setCart_id(String cart_id) {
        this.cart_id = cart_id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getQuentity() {
        return quentity;
    }

    public void setQuentity(String quentity) {
        this.quentity = quentity;
    }

    public String getVendor_name() {
        return vendor_name;
    }

    public void setVendor_name(String vendor_name) {
        this.vendor_name = vendor_name;
    }

    public String getPrdct_id() {
        return prdct_id;
    }

    public void setPrdct_id(String prdct_id) {
        this.prdct_id = prdct_id;
    }

    public String getVendor_id() {
        return vendor_id;
    }

    public void setVendor_id(String vendor_id) {
        this.vendor_id = vendor_id;
    }

    public String getMed_name() {
        return med_name;
    }

    public void setMed_name(String med_name) {
        this.med_name = med_name;
    }

    public String getSale_price() {
        return sale_price;
    }

    public void setSale_price(String sale_price) {
        this.sale_price = sale_price;
    }

    public String getMrp() {
        return mrp;
    }

    public void setMrp(String mrp) {
        this.mrp = mrp;
    }

    public String getAdditional_offer_percentage() {
        return additional_offer_percentage;
    }

    public void setAdditional_offer_percentage(String additional_offer_percentage) {
        this.additional_offer_percentage = additional_offer_percentage;
    }

    public String getAdditional_offer_id() {
        return additional_offer_id;
    }

    public void setAdditional_offer_id(String additional_offer_id) {
        this.additional_offer_id = additional_offer_id;
    }

    public String getAdditional_offer_code() {
        return additional_offer_code;
    }

    public void setAdditional_offer_code(String additional_offer_code) {
        this.additional_offer_code = additional_offer_code;
    }

    public String getAdditional_offer_decription() {
        return additional_offer_decription;
    }

    public void setAdditional_offer_decription(String additional_offer_decription) {
        this.additional_offer_decription = additional_offer_decription;
    }

    public String getAdditional_offer_start() {
        return additional_offer_start;
    }

    public void setAdditional_offer_start(String additional_offer_start) {
        this.additional_offer_start = additional_offer_start;
    }

    public String getAdditional_offer_expired() {
        return additional_offer_expired;
    }

    public void setAdditional_offer_expired(String additional_offer_expired) {
        this.additional_offer_expired = additional_offer_expired;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getCompany_name() {
        return company_name;
    }

    public void setCompany_name(String company_name) {
        this.company_name = company_name;
    }

    public String getAdditional_offer_type() {
        return additional_offer_type;
    }

    public void setAdditional_offer_type(String additional_offer_type) {
        this.additional_offer_type = additional_offer_type;
    }

    public String getAdditional_offer() {
        return additional_offer;
    }

    public void setAdditional_offer(String additional_offer) {
        this.additional_offer = additional_offer;
    }
}
