package com.eme22.applicacioncomida.data.api;

import com.eme22.applicacioncomida.data.api.WebApiService;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class WebApiAdapter {

    public static final String API_URL = "https://webapitest-production.up.railway.app";

    private static WebApiService API_SERVICE;

    private WebApiAdapter() {
    }

    public static WebApiService getApiService() {

        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.level(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient.Builder httpClient = new OkHttpClient.Builder()
                .addInterceptor(logging);
        if (API_SERVICE == null) {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(API_URL)
                    .addConverterFactory(ScalarsConverterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(httpClient.build())
                    .build();
            API_SERVICE = retrofit.create(WebApiService.class);
        }


        return API_SERVICE;
    }
}
