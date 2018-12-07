package com.tiendas3b.almacen.http;

import android.util.Base64;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.tiendas3b.almacen.shipment.http.ShipmentService;
import com.tiendas3b.almacen.util.Constants;

import java.io.UnsupportedEncodingException;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by dfa on 22/02/2016.
 */
@SuppressWarnings("DefaultFileTemplate")
public class ServiceGenerator {

    private static final String AUTHORIZATION = "Authorization";
    private static OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

    private static Gson gson = new GsonBuilder().setDateFormat(Constants.DATETIME_FORMAT_WS).create();//"yyyy'-'MM'-'dd'T'HH':'mm':'ss'.'SSS'Z'"
    private static Gson gsonTime = new GsonBuilder().setDateFormat(Constants.DATETIME_FORMAT_WS_TIME).create();

    private static Retrofit.Builder builder = new Retrofit.Builder().addConverterFactory(GsonConverterFactory.create(gson));
    private static Retrofit.Builder builderTime = new Retrofit.Builder().addConverterFactory(GsonConverterFactory.create(gsonTime));

    public static <S> S createService(Class<S> serviceClass, String login, String password, String url) {
        authBasic(login, password);
        OkHttpClient client = httpClient.build();
        Retrofit retrofit = builder.baseUrl(url).client(client).build();
        return retrofit.create(serviceClass);
    }

    public static <S> S createServiceTime(Class<S> serviceClass, String login, String password, String url) {
        authBasic(login, password);
        httpClient.connectTimeout(5, TimeUnit.SECONDS);//10 default
        httpClient.readTimeout(300, TimeUnit.SECONDS);
        httpClient.writeTimeout(60, TimeUnit.SECONDS);
        OkHttpClient client = httpClient.build();
        Retrofit retrofit = builderTime.baseUrl(url).client(client).build();
        return retrofit.create(serviceClass);
    }

    public static <S> S createServiceTimeGsonTime(Class<S> serviceClass, String login, String password, String url) {
        authBasic(login, password);
        httpClient.connectTimeout(5, TimeUnit.SECONDS);//10 default
        httpClient.readTimeout(300, TimeUnit.SECONDS);
        httpClient.writeTimeout(60, TimeUnit.SECONDS);
        OkHttpClient client = httpClient.build();
        Retrofit retrofit = builder.baseUrl(url).client(client).build();
        return retrofit.create(serviceClass);
    }

    private static void authBasic(String login, String password) {
        if (login != null && password != null) {
            String credentials = login + ":" + password;
            final String basic;
            try {
                byte[] bytes = credentials.getBytes("UTF-8");
                basic = "Basic " + Base64.encodeToString(bytes, Base64.NO_WRAP);
                httpClient.addInterceptor(chain -> {
                    Request original = chain.request();
                    Request.Builder requestBuilder = original.newBuilder().header(AUTHORIZATION, basic).header("Accept", "application/json").method(original.method(), original.body());
                    Request request = requestBuilder.build();
                    return chain.proceed(request);
                });
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
    }

    public static Tiendas3bClient createService(Class<Tiendas3bClient> serviceClass, String url) {
        httpClient.connectTimeout(5, TimeUnit.SECONDS);//10 default
        httpClient.readTimeout(240, TimeUnit.SECONDS);
        httpClient.writeTimeout(60, TimeUnit.SECONDS);
        OkHttpClient client = httpClient.build();
        Retrofit retrofit = builderTime.baseUrl(url).client(client).build();
        return retrofit.create(serviceClass);
    }

    public static ShipmentService createShipmentService(Class<ShipmentService> shipmentServiceClass, String url) {
        Gson gson = new GsonBuilder().serializeNulls().setDateFormat(Constants.DATETIME_FORMAT_WS).create();
        Retrofit.Builder retrofitBuilder = new Retrofit.Builder().addConverterFactory(GsonConverterFactory.create(gson));


        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        httpClient.connectTimeout(5, TimeUnit.SECONDS);//10 default
        httpClient.readTimeout(240, TimeUnit.SECONDS);
        httpClient.writeTimeout(60, TimeUnit.SECONDS);
        httpClient.addInterceptor(interceptor);
        OkHttpClient client = httpClient.build();
        Retrofit retrofit = retrofitBuilder.baseUrl(url).client(client).build();
        return retrofit.create(shipmentServiceClass);
    }

}

