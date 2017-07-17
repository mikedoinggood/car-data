package com.glicerial.samples.cardata.network;


import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import okhttp3.logging.HttpLoggingInterceptor.Level;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ServiceGenerator {
    // IP address is special alias to emulator's host
    public static final String API_BASE_URL = "http://10.0.2.2:8081";

    private static Retrofit.Builder builder = new Retrofit.Builder().baseUrl(API_BASE_URL).addConverterFactory(GsonConverterFactory.create());
    private static OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

    public static void enableHttpLogging() {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        // set your desired log level
        logging.setLevel(Level.BODY);

        // add logging as last interceptor
        httpClient.addInterceptor(logging);  // <-- this is the important line!
        builder.client(httpClient.build()).build();
    }

    public static <S> S createService(Class<S> serviceClass) {
        Retrofit retrofit = builder.build();
        return retrofit.create(serviceClass);
    }
}
