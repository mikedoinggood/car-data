package com.glicerial.samples.cardata.network;


import com.glicerial.samples.cardata.model.Car;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface CarClient {

    @GET("/api/cars")
    Call<List<Car>> getCars(@Header("Authorization") String authHeader);

    @POST("/api/cars")
    Call<Car> addCar(@Header("Authorization") String authHeader, @Body Car car);

}
