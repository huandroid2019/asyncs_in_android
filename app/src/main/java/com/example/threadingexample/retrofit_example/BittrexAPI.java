package com.example.threadingexample.retrofit_example;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface BittrexAPI {
    @GET("getmarkets")
    Call<ResultObject> getMarkets();


}
