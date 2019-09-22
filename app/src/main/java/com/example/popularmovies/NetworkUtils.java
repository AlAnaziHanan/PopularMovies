package com.example.popularmovies;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class NetworkUtils {
    private static final String TAG = NetworkUtils.class.getSimpleName ();

    public static ArrayList<RetroPhoto> fetchData ( String url ) throws IOException {
        ArrayList<RetroPhoto> m = new ArrayList<> ();
        try {
            URL newURL = new URL ( url );
            HttpURLConnection conn = (HttpURLConnection) newURL.openConnection ();
            conn.connect ();
            InputStream input = conn.getInputStream ();
            String data = IOUtils.toString ( input );
            parseJson ( data , m );
            input.close ();

        } catch (IOException e) {
            e.printStackTrace ();
        }
        return m;

    }

    //check connectivity
    public static Boolean status ( Context context ) {
        ConnectivityManager manager = (ConnectivityManager) context.getSystemService ( Context.CONNECTIVITY_SERVICE );
        NetworkInfo netInfo = manager.getActiveNetworkInfo ();
        return netInfo != null && netInfo.isConnected ();
    }

    private static void parseJson ( String data , ArrayList<RetroPhoto> m ) {

        try {
            JSONObject obj = new JSONObject ( data );
            JSONArray array = obj.getJSONArray ( "results" );

            for (int i = 0; i < array.length (); i++) {
                JSONObject json = array.getJSONObject ( i );
                RetroPhoto movie = new RetroPhoto ();

                //set movie details
                movie.setTitle ( json.getString ( "title" ) );
                movie.setDate ( json.getString ( "release_date" ) );
                movie.setVote_average ( json.getString ( "vote_average" ) );
                movie.setOverview ( json.getString ( "overview" ) );
                movie.setPosterPath ( json.getString ( "poster_path" ) );
                //  movie.setPopularity ( json.getDouble ( "popularity" ) );
                //add movie to m ArrayList
                m.add ( movie );
            }
        } catch (JSONException e) {
            e.printStackTrace ();
            Log.e ( TAG , "Error occurred during JSON Parse" , e );
        }
    }


}