package com.medical.product.models;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Blog {

    @SerializedName("status")
    @Expose
    private Boolean status;
    @SerializedName("blog-list")
    @Expose
    private List<BlogList> blogList = null;

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public List<BlogList> getBlogList() {
        return blogList;
    }

    public void setBlogList(List<BlogList> blogList) {
        this.blogList = blogList;
    }

}