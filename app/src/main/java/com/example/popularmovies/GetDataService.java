package com.example.popularmovies;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

interface GetDataService {
    @GET("/photos")
    Call<List<Movie>> getAllPhotos();
}

