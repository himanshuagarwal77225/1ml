package com.medical.product.helpingFile;

import com.medical.product.Interfaces.OfferServiceApi;

public class ApiUtils {
    public static final String BASE_URL = "https://www.thyrocare.com/APIS/";
    public static final String BASE_URL_BETA = "https://www.thyrocare.com/APIS/";
    public static final String EMAIL_URL_BETA = "https://mail.zoho.com/";
    public static final String BASE_URL_BLOOD = "http://www.1ml.co.in/demo/api/";
    public static final String DocsApp_URL = "https://testweb.docsapp.in/";

    public static OfferServiceApi getOfferService() {
        return RetrofitClient.getClient(BASE_URL).create(OfferServiceApi.class);
    }

    public static OfferServiceApi getOfferServiceBlood() {
        return RetrofitClient.getClient(BASE_URL_BLOOD).create(OfferServiceApi.class);
    }

    public static OfferServiceApi postFixAppointment() {
        return RetrofitClient.getClient(BASE_URL_BETA).create(OfferServiceApi.class);
    }

    public static OfferServiceApi postPostOrderData() {
        return RetrofitClient.getClient(BASE_URL_BETA).create(OfferServiceApi.class);
    }

    public static OfferServiceApi postEmail() {
        return RetrofitClient.getClient(EMAIL_URL_BETA).create(OfferServiceApi.class);
    }

    public static OfferServiceApi getDocsUrl() {
        return RetrofitClient.getClient(DocsApp_URL).create(OfferServiceApi.class);
    }

    public static OfferServiceApi getBlog() {
        return RetrofitClient.getClient(BASE_URL_BLOOD).create(OfferServiceApi.class);
    }
}