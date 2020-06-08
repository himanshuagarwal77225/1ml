package com.medical.product.models;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ORDERRESPONSE {

    @SerializedName("PostOrderDataResponse")
    @Expose
    private List<PostOrderDataResponse> postOrderDataResponse = null;

    public List<PostOrderDataResponse> getPostOrderDataResponse() {
        return postOrderDataResponse;
    }

    public void setPostOrderDataResponse(List<PostOrderDataResponse> postOrderDataResponse) {
        this.postOrderDataResponse = postOrderDataResponse;
    }

}