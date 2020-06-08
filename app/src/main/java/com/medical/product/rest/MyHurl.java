package com.medical.product.rest;

import android.content.Context;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.toolbox.HttpResponse;
import com.android.volley.toolbox.HurlStack;
import com.medical.product.helpingFile.DroiderApplication;
import com.medical.product.helpingFile.OneMLUserSpace;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class MyHurl extends HurlStack {

    @Override
    public HttpResponse executeRequest(Request<?> request, Map<String, String> additionalHeaders){
        OneMLUserSpace yoLearnUserSpace = OneMLUserSpace.getInstance(DroiderApplication.getInstance());
        final String api_key = yoLearnUserSpace.getToken();
        final String api_id = yoLearnUserSpace.get1MLUserID();

        Map<String, String> headers = new HashMap<>(additionalHeaders);
        headers.put("x-api-key", api_key);
        headers.put("x-api-id", api_id);



        try {
            return super.executeRequest(request, headers);
        } catch (IOException e) {

        } catch (AuthFailureError authFailureError) {
        }

        return null;
    }
}
