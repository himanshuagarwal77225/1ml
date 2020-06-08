package com.medical.product.models;

public class GatterGetFamilyDocumentListModel {

    private String id,family_id,document_image,document_title,document_description,type,dr_name;

    public GatterGetFamilyDocumentListModel(String id, String family_id, String document_image, String document_title, String document_description, String dr_name,String type) {
        this.id = id;
        this.family_id = family_id;
        this.document_image = document_image;
        this.document_title = document_title;
        this.document_description = document_description;
        this.dr_name=dr_name;
        this.type = type;
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

    public String getDr_name() {
        return dr_name;
    }

    public void setDr_name(String dr_name) {
        this.dr_name = dr_name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
