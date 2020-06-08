package com.medical.product.models;

public class GatterGetSimilorList {
    String id,unique_code,vendor_id,vendor_name,pd_brand_name,pd_company_name,image,brand_name,company_name,mrp,sale_price;

    public GatterGetSimilorList(String id, String unique_code, String vendor_id, String vendor_name, String pd_brand_name, String pd_company_name, String image, String brand_name, String company_name, String mrp, String sale_price) {
        this.id = id;
        this.unique_code = unique_code;
        this.vendor_id = vendor_id;
        this.vendor_name = vendor_name;
        this.pd_brand_name = pd_brand_name;
        this.pd_company_name = pd_company_name;
        this.image = image;
        this.brand_name = brand_name;
        this.company_name = company_name;
        this.mrp = mrp;
        this.sale_price = sale_price;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUnique_code() {
        return unique_code;
    }

    public void setUnique_code(String unique_code) {
        this.unique_code = unique_code;
    }

    public String getVendor_id() {
        return vendor_id;
    }

    public void setVendor_id(String vendor_id) {
        this.vendor_id = vendor_id;
    }

    public String getVendor_name() {
        return vendor_name;
    }

    public void setVendor_name(String vendor_name) {
        this.vendor_name = vendor_name;
    }

    public String getPd_brand_name() {
        return pd_brand_name;
    }

    public void setPd_brand_name(String pd_brand_name) {
        this.pd_brand_name = pd_brand_name;
    }

    public String getPd_company_name() {
        return pd_company_name;
    }

    public void setPd_company_name(String pd_company_name) {
        this.pd_company_name = pd_company_name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getBrand_name() {
        return brand_name;
    }

    public void setBrand_name(String brand_name) {
        this.brand_name = brand_name;
    }

    public String getCompany_name() {
        return company_name;
    }

    public void setCompany_name(String company_name) {
        this.company_name = company_name;
    }

    public String getMrp() {
        return mrp;
    }

    public void setMrp(String mrp) {
        this.mrp = mrp;
    }

    public String getSale_price() {
        return sale_price;
    }

    public void setSale_price(String sale_price) {
        this.sale_price = sale_price;
    }

}
