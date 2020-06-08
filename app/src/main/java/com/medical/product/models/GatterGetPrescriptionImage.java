package com.medical.product.models;

import java.io.Serializable;

public class GatterGetPrescriptionImage{

    String id,arrid,image,type;

    public GatterGetPrescriptionImage(String id, String arrid, String image, String type) {
        this.id = id;
        this.arrid = arrid;
        this.image = image;
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getArrid() {
        return arrid;
    }

    public void setArrid(String arrid) {
        this.arrid = arrid;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
