package com.medical.product.models;

import java.util.ArrayList;

public class VerticalModel {

    String title;
    ArrayList<HorizontalModelBrands> arrayList;

    ArrayList<HorizontalModelCategory> arrayListCategory;

    ArrayList<HorizontalModelProducts> arrayListProducts;
    ArrayList<Adf> arrayListad;

    public ArrayList<HorizontalModelProducts> getArrayListProducts() {
        return arrayListProducts;
    }

    public void setArrayListProducts(ArrayList<HorizontalModelProducts> arrayListProducts) {
        this.arrayListProducts = arrayListProducts;
    }

    public ArrayList<HorizontalModelCategory> getArrayListCategory() {
        return arrayListCategory;
    }

    public void setArrayListCategory(ArrayList<HorizontalModelCategory> arrayListCategory) {
        this.arrayListCategory = arrayListCategory;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public ArrayList<HorizontalModelBrands> getArrayList() {
        return arrayList;
    }

    public void setArrayList(ArrayList<HorizontalModelBrands> arrayList) {
        this.arrayList = arrayList;
    }

    public ArrayList<Adf> getArrayListad() {
        return arrayListad;
    }

    public void setArrayListad(ArrayList<Adf> arrayListad) {
        this.arrayListad = arrayListad;
    }
}
