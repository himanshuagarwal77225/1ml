
package com.medical.product.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ImageMaster {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("img_locations")
    @Expose
    private String imgLocations;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImgLocations() {
        return imgLocations;
    }

    public void setImgLocations(String imgLocations) {
        this.imgLocations = imgLocations;
    }

    @Override
    public String toString()
    {
        return "ImageMaster [img_locations = "+imgLocations+", id = "+id+"]";
    }
}
