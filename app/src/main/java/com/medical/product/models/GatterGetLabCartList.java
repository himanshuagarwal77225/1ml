package com.medical.product.models;


public class GatterGetLabCartList {
    public String multiple;

    public String getMultiple() {
        return multiple;
    }

    public void setMultiple(String multiple) {
        this.multiple = multiple;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    private String additional_discount;
    private String imageUrl;
    public String getAdditional_discount() {
        return additional_discount;
    }

    public void setAdditional_discount(String additional_discount) {
        this.additional_discount = additional_discount;
    }

    public int getChild() {
        return child;
    }

    public void setChild(int child) {
        this.child = child;
    }

    public GatterGetLabCartList(){

    }
    private int child;
    public GatterGetLabCartList(String multiple,String imageUrl,int child, String price_final,String additional_discount,String date, String discount, String pid, String price, String product, String fasting, String plan_status,String plan_value) {
        this.plan_status=plan_status;
        this.child=child;
        this.plan_value=plan_value;
        this.date = date;
        this.multiple=multiple;
        this.additional_discount=additional_discount;
        this.discount = discount;
        this.fasting=fasting;
        this.pid = pid;
        this.price = price;
        this.product = product;
        this.imageUrl = imageUrl;
    }

    private String date;

    public void setPlan_status(String plan_status) {
        this.plan_status = plan_status;
    }

    public void setPlan_value(String plan_value) {
        this.plan_value = plan_value;
    }

    public String getPlan_status() {
        return plan_status;
    }

    public String getPlan_value() {
        return plan_value;
    }

    private String plan_status;
    private String plan_value;


    public String getFasting() {
        return fasting;
    }

    public void setFasting(String fasting) {
        this.fasting = fasting;
    }

    private String fasting;
    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    private String discount;
    private String pid;
    private String price;
    private String product;

}