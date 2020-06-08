package com.medical.product.rest;

import androidx.annotation.NonNull;

import com.medical.product.models.Blog;
import com.medical.product.models.BloodBank;
import com.medical.product.models.UserRegistrationResponseModel;

import java.util.Map;

import io.reactivex.Observable;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface ApiInterface {

    @NonNull
    @FormUrlEncoded
    @POST("user/set")
    Call<UserRegistrationResponseModel>
    postRegisterUser(@FieldMap Map<String, String> fields);

    @NonNull
    @FormUrlEncoded
    @POST("user/loginu")
    Call<UserRegistrationResponseModel>
    postLoginUser(@FieldMap Map<String, String> fields);

    @GET("product/blog")
    Observable<Blog> getBlog();

    @POST("product/blood-banks")
    @FormUrlEncoded
    Observable<BloodBank> getNearbyBloodBank(@Field("latitude") String latitude, @Field("longitude") String longitude, @Field("range") String range);
}
