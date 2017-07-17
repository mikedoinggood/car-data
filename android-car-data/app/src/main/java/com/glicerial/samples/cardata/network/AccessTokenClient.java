package com.glicerial.samples.cardata.network;


import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface AccessTokenClient {
    @FormUrlEncoded
    @POST("/oauth/token")
    Call<AccessToken> getPasswordGrantAccessToken(
            @Field("username") String username,
            @Field("password") String password,
            @Field("grant_type") String grantType,
            @Field("scope") String scope,
            @Field("client_id") String clientId);

}
