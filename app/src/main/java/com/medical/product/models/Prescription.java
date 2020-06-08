package com.medical.product.models;

import java.util.ArrayList;

public class Prescription {
   String id,order_id,user_approved,price_total,delevery_date,shipping_charge,perception_detail,add_date,for_vendor,perception_method,approved;
   String[] perception_images;
   ArrayList<Approved_data_detail>  approved_data_detail;
   ArrayList<Vendors> vendors ;
   GatterGetAddressListModel shipping_address;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    public String getUser_approved() {
        return user_approved;
    }

    public void setUser_approved(String user_approved) {
        this.user_approved = user_approved;
    }

    public String getPrice_total() {
        return price_total;
    }

    public void setPrice_total(String price_total) {
        this.price_total = price_total;
    }

    public String getDelevery_date() {
        return delevery_date;
    }

    public void setDelevery_date(String delevery_date) {
        this.delevery_date = delevery_date;
    }

    public String getShipping_charge() {
        return shipping_charge;
    }

    public void setShipping_charge(String shipping_charge) {
        this.shipping_charge = shipping_charge;
    }

    public String[] getPerception_images() {
        return perception_images;
    }

    public void setPerception_images(String[] perception_images) {
        this.perception_images = perception_images;
    }

    public ArrayList<Approved_data_detail> getApproved_data_detail() {
        return approved_data_detail;
    }

    public void setApproved_data_detail(ArrayList<Approved_data_detail> approved_data_detail) {
        this.approved_data_detail = approved_data_detail;
    }

    public ArrayList<Vendors> getVendors() {
        return vendors;
    }

    public void setVendors(ArrayList<Vendors> vendors) {
        this.vendors = vendors;
    }

    public String getPerception_detail() {
        return perception_detail;
    }

    public void setPerception_detail(String perception_detail) {
        this.perception_detail = perception_detail;
    }

    public String getAdd_date() {
        return add_date;
    }

    public void setAdd_date(String add_date) {
        this.add_date = add_date;
    }

    public String getFor_vendor() {
        return for_vendor;
    }

    public void setFor_vendor(String for_vendor) {
        this.for_vendor = for_vendor;
    }

    public String getPerception_method() {
        return perception_method;
    }

    public void setPerception_method(String perception_method) {
        this.perception_method = perception_method;
    }

    public String getApproved() {
        return approved;
    }

    public void setApproved(String approved) {
        this.approved = approved;
    }

    public GatterGetAddressListModel getShipping_address() {
        return shipping_address;
    }

    public void setShipping_address(GatterGetAddressListModel shipping_address) {
        this.shipping_address = shipping_address;
    }
}
