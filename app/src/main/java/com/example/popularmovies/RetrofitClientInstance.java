package com.example.popularmovies;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

class RetrofitClientInstance {
    private static Retrofit retrofit;


    static Retrofit getRetrofitInstance ( String url ){

        if(retrofit==null){
            retrofit = new retrofit2.Retrofit.Builder ()
                    .baseUrl ( url )
                    .addConverterFactory ( GsonConverterFactory.create () )
                    .build ();
        }return retrofit;
    }
}