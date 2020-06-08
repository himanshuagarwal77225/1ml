package com.medical.product.Interfaces;


import com.medical.product.models.All;
import com.medical.product.models.Blog;
import com.medical.product.models.BloodBank;
import com.medical.product.models.DocsRequest;
import com.medical.product.models.DocsResponse;
import com.medical.product.models.Duplicate;
import com.medical.product.models.EmailRequest;
import com.medical.product.models.EmailResponse;
import com.medical.product.models.FixAppointmentModelRequest;
import com.medical.product.models.FixAppointmentModelResponse;
import com.medical.product.models.Pincode;
import com.medical.product.models.PostOrderDataResponse;
import com.medical.product.models.PostOrderRequestModel;
import com.medical.product.models.PostOrderResponseModel;
import com.medical.product.models.Slot;

import org.checkerframework.checker.nullness.compatqual.PolyNullDecl;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;


public interface OfferServiceApi {

    @GET("master.svc/{API}/all/products")
    Observable<All> getAll(@Path("API") String api);

    @GET("order.svc/oKtp1d0l0SMZ9FtdQmwP0agrI39tKJATf0unh8fpQNYcKLWvXyFzEw==/{pincode}/PincodeAvailability")
    Observable<Pincode> getAvailability(@Path("pincode") String pincode);

    @GET("order.svc/{pincode}/{date}/GetAppointmentSlots")
    Observable<Slot> getSlotAvailability(@Path("pincode") String pincode,@Path("date") String date);

    @POST("product/blood-banks")
    @FormUrlEncoded
    Observable<BloodBank> getNearbyBloodBank(@Field("latitude") String latitude,@Field("longitude") String longitude,@Field("range") String range);

    @GET("ORDER.svc/{api_key}/{testnames}/GetDuplicateTest")
    Observable<Duplicate> getDuplicateTests(@Path("api_key") String api,@Path("testnames") String testnames);

    @POST("ORDER.svc/FixAppointment")
    Observable<FixAppointmentModelResponse> fixAppointment(@Body FixAppointmentModelRequest fixAppointmentModelRequest);

    @POST("ORDER.svc/Postorderdata")
    Observable<PostOrderResponseModel> postorderata(@Body PostOrderRequestModel postOrderRequestModel);

    @Headers({"Content-Type:application/json",
            "Authorization:6f4e56d678c0bcc529949cd9faa5951f"})
    @POST("api/accounts/3328095000000008002/messages")
    Observable<EmailResponse> postEmail(@Body EmailRequest emailRequest);

    @Headers({"token:05181c54-7925-4550-ad16-c66c7c9767f4",
    "Content-Type:application/json"})
    @POST("patientswebapp/vendor/generate")
    Observable<DocsResponse> getDocsUrl(@Body DocsRequest docsRequest);


    @GET("product/blog")
    Observable<Blog> getBlog();


}
