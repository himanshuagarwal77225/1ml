package com.medical.product.models;

import java.io.Serializable;

public class GatterGetPrescriptionModel implements Serializable{
    private boolean isSelected;
    private String id,family_id,document_image,document_title,document_description,type;

    public GatterGetPrescriptionModel(boolean isSelected, String id, String family_id, String document_image, String document_title, String document_description, String type) {
        this.isSelected = isSelected;
        this.id = id;
        this.family_id = family_id;
        this.document_image = document_image;
        this.document_title = document_title;
        this.document_description = document_description;
        this.type = type;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public Boolean setSelected(boolean selected) {
        isSelected = selected;
        return isSelected;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFamily_id() {
        return family_id;
    }

    public void setFamily_id(String family_id) {
        this.family_id = family_id;
    }

    public String getDocument_image() {
        return document_image;
    }

    public void setDocument_image(String document_image) {
        this.document_image = document_image;
    }

    public String getDocument_title() {
        return document_title;
    }

    public void setDocument_title(String document_title) {
        this.document_title = document_title;
    }

    public String getDocument_description() {
        return document_description;
    }

    public void setDocument_description(String document_description) {
        this.document_description = document_description;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
