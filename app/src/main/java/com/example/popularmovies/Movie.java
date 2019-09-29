package com.example.popularmovies;

import com.google.gson.annotations.SerializedName;

class Movie {

    @SerializedName( "title" )
    private String title;
    @SerializedName ( "vote_average" )
    private String vote_average;
    @SerializedName ( "poster_path" )
    private String posterPath;
    @SerializedName ( "release_date" )
    private String date;
    @SerializedName ( "overview" )
    private String overview;

    public Movie ( String title , String vote_average , String posterPath , String date , String overview ) {
        this.title = title;
        this.vote_average = vote_average;
        this.posterPath = posterPath;
        this.date = date;
        this.overview = overview;
    }

    public Movie () {

    }

    public String getTitle () {
        return title;
    }

    public void setTitle ( String title ) {
        this.title = title;
    }

    public String getVote_average () {
        return vote_average;
    }

    public void setVote_average ( String vote_average ) {
        this.vote_average = vote_average;
    }

    public String getPosterPath () {
        return posterPath;
    }

    public void setPosterPath ( String posterPath ) {
        this.posterPath = posterPath;
    }

    public String getDate () {
        return date;
    }

    public void setDate ( String date ) {
        this.date = date;
    }

    public String getOverview () {
        return overview;
    }

    public void setOverview ( String overview ) {
        this.overview = overview;
    }
}