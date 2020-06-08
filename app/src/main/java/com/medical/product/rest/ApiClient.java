package com.medical.product.rest;


import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.medical.product.helpingFile.ApiFileuri;
import com.medical.product.helpingFile.ApiUtils;
import com.medical.product.helpingFile.OneMLUserSpace;

import java.util.concurrent.TimeUnit;

import okhttp3.CookieJar;
import okhttp3.Credentials;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.jackson.JacksonConverterFactory;

public class ApiClient {


    @Nullable
    private static Retrofit retrofit = null;
    @NonNull
    private static OkHttpClient.Builder httpClient = new OkHttpClient.Builder().connectTimeout(2, TimeUnit.MINUTES)
            .readTimeout(1, TimeUnit.MINUTES)
            .writeTimeout(1, TimeUnit.MINUTES);
    @NonNull
    private static Retrofit.Builder builder =
            new Retrofit.Builder()
                    .baseUrl(ApiFileuri.ROOT_HTTP_URL)
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .addConverterFactory(JacksonConverterFactory.create());
    ApiClient apiClient;

    @Nullable
    public static Retrofit getClient() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(ApiFileuri.ROOT_HTTP_URL)
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .addConverterFactory(JacksonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }

    @NonNull
    public static <S> S createService(@NonNull Class<S> serviceClass) {
        return createService(serviceClass, null, null, null);

    }

    @NonNull
    public static <S> S createServices(@NonNull Class<S> serviceClass, Context context) {
        OneMLUserSpace yoLearnUserSpace = OneMLUserSpace.getInstance(context);
        final String token = yoLearnUserSpace.getBearerToken();
        httpClient.addInterceptor(chain -> {
            Request original = chain.request();
            Request.Builder requestBuilder = original.newBuilder()
                    .header("Accept", "accept/json")
                    .header("Authorization", token)
                    .method(original.method(), original.body());
            Request request = requestBuilder.build();
            return chain.proceed(request);
        });
        OkHttpClient client = httpClient.build();
        retrofit = builder.client(client).addCallAdapterFactory(RxJava2CallAdapterFactory.create()).build();
        return retrofit.create(serviceClass);
    }

    @NonNull
    public static <S> S createService(@NonNull Class<S> serviceClass, Context context) {
        OneMLUserSpace yoLearnUserSpace = OneMLUserSpace.getInstance(context);
        final String api_key = yoLearnUserSpace.getToken();
        final String api_id = yoLearnUserSpace.get1MLUserID();
        httpClient.addInterceptor(chain -> {
            Request original = chain.request();
            Request.Builder requestBuilder = original.newBuilder()
                    .header("Accept", "accept/json")
                    .header("x-api-key", api_key)
                    .header("x-api-id", api_id)
                    .method(original.method(), original.body());
            Request request = requestBuilder.build();
            return chain.proceed(request);
        });
        OkHttpClient client = httpClient.build();
        retrofit = builder.client(client).addCallAdapterFactory(RxJava2CallAdapterFactory.create()).build();
        return retrofit.create(serviceClass);
    }

    @NonNull
    public static <S> S createService(@NonNull Class<S> serviceClass, @Nullable String username, @Nullable String password, Context context) {

        if (username != null && password != null) {
            String authToken = Credentials.basic(username, password);
            String credentials = username + ":" + password;
            httpClient.addInterceptor(chain -> {
                Request original = chain.request();
                Request.Builder requestBuilder = original.newBuilder()
                        .addHeader("Accept", "text/json")
                        .header("Authorization", authToken)
                        .method(original.method(), original.body());
                Request request = requestBuilder.build();
                return chain.proceed(request);
            });
        }
        OkHttpClient client = httpClient.cookieJar(CookieJar.NO_COOKIES)
                .build();
        retrofit = builder.client(client).addCallAdapterFactory(RxJava2CallAdapterFactory.create()).build();
        return retrofit.create(serviceClass);
    }

    @NonNull
    public static <S> S createServiceVideo(@NonNull Class<S> serviceClass, @Nullable String username, @Nullable String password, Context context, String fileName) {

        if (username != null && password != null) {
            String authToken = Credentials.basic(username, password);
            String credentials = username + ":" + password;
            httpClient.addInterceptor(chain -> {
                Request original = chain.request();
                Request.Builder requestBuilder = original.newBuilder()
                        .addHeader("Accept", "text/json")
                        .header("Authorization", authToken)
                        .header("Content-Type", "mp4")
                        .header("Content-Disposition", "attachment; filename=" + fileName)
                        .method(original.method(), original.body());
                Request request = requestBuilder.build();
                return chain.proceed(request);
            });
        }
        OkHttpClient client = httpClient.cookieJar(CookieJar.NO_COOKIES)
                .connectTimeout(5, TimeUnit.MINUTES)
                .readTimeout(5, TimeUnit.MINUTES)
                .writeTimeout(5, TimeUnit.MINUTES)
                .build();
        retrofit = builder.client(client).addCallAdapterFactory(RxJava2CallAdapterFactory.create()).build();
        return retrofit.create(serviceClass);
    }

    @NonNull
    public static <S> S createServiceImage(@NonNull Class<S> serviceClass, @Nullable String username, @Nullable String password, Context context, String fileName) {

        if (username != null && password != null) {
            String authToken = Credentials.basic(username, password);
            String credentials = username + ":" + password;
            httpClient.addInterceptor(chain -> {
                Request original = chain.request();
                Request.Builder requestBuilder = original.newBuilder()
                        .addHeader("Accept", "text/json")
                        .header("Authorization", authToken)
                        .header("Content-Type", "image")
                        .header("Content-Disposition", "attachment; filename=" + fileName)
                        .method(original.method(), original.body());
                Request request = requestBuilder.build();
                return chain.proceed(request);
            });
        }
        OkHttpClient client = httpClient.cookieJar(CookieJar.NO_COOKIES)
                .connectTimeout(5, TimeUnit.MINUTES)
                .readTimeout(5, TimeUnit.MINUTES)
                .writeTimeout(5, TimeUnit.MINUTES)
                .build();
        retrofit = builder.client(client).addCallAdapterFactory(RxJava2CallAdapterFactory.create()).build();
        return retrofit.create(serviceClass);
    }

    public static void changeApiBaseUrl(@NonNull String newApiBaseUrl) {
        builder = new Retrofit.Builder()
                .addConverterFactory(JacksonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .baseUrl(newApiBaseUrl);
    }
    public static void changeConvertorToGSON(@NonNull String newApiBaseUrl){
        builder = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .baseUrl(newApiBaseUrl);
    }
}
