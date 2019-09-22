package com.example.popularmovies;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClientInstance {
    private static Retrofit retrofit;
    private static final String BASE_URL = "https://api.themoviedb.org/3/movie/550?api_key=a1929f608371156c06e3be63aca37892";

    public static Retrofit getRetrofitInstance(){

        if(retrofit==null){
            retrofit = new retrofit2.Retrofit.Builder ()
                    .baseUrl ( BASE_URL )
                    .addConverterFactory ( GsonConverterFactory.create () )
                    .build ();
        }return retrofit;
    }
}