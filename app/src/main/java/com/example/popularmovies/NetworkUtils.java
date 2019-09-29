package com.example.popularmovies;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.GridView;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

//import org.apache.commons.io.IOUtils;

public class NetworkUtils extends AppCompatActivity {
    private static final String TAG = NetworkUtils.class.getSimpleName ();
    public static final String BASE_URL = "https://api.themoviedb.org/v5/";
   // public static final String POPURL="http://api.themoviedb.org/3/discover/movie?sort_by=popularity.desc&api_key=/";//+key;;
    //public static final String RATEURL="http://api.themoviedb.org/3/discover/movie?sort_by=vote_average.desc&api_key=/";//+key;;
    private GridView mGridView;
    private ProgressBar progressBar;
    private GridViewAdapter mGridAdapter;
    private ArrayList<Movie> mGridData;

    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gridview);
        mGridView = findViewById(R.id.moviesGrid);
        progressBar = findViewById(R.id.bar);
        //Initialize
        mGridData = new ArrayList<>();
        mGridAdapter = new GridViewAdapter(this, R.layout.model, mGridData);
        mGridView.setAdapter(mGridAdapter);
        //Start download
        new Json().execute ();
        progressBar.setVisibility( View.VISIBLE);
    }

    private class Json extends AsyncTask<Void, Void, ArrayList<Movie>> {

        @Override
        protected ArrayList<Movie> doInBackground ( Void... voids ) {
            URLConnection urlConnection=null;
            BufferedReader bufferedReader=null;
            ArrayList<Movie> m = new ArrayList<> ();
            try {
                URL movieURL=new URL(BASE_URL);
                urlConnection=movieURL.openConnection ();
                bufferedReader =new BufferedReader ( new InputStreamReader ( urlConnection.getInputStream () ) );
                StringBuffer stringBuffer= new StringBuffer (  );

                String line;
                while((line=bufferedReader.readLine ())!=null){
                    stringBuffer.append ( line );
                }
                //return stringBuffer.toString ();
            } catch (IOException e) {
                e.printStackTrace ();
            }
            return m;
        }
        /*@Override
        protected void onPostExecute ( Integer result ) {
            if (result == 1) {
                mGridAdapter.setGridData(mGridData);
            } else {
                Toast.makeText(NetworkUtils.this, "Failed to fetch data!", Toast.LENGTH_SHORT).show();
            }
            progressBar.setVisibility(View.GONE);
        }*/
    }
    String streamToString  ( InputStream stream ) throws IOException {
        ArrayList<Movie> m = new ArrayList<> ();
        URLConnection urlConnection=null;
        BufferedReader bufferedReader=null;
        ProgressDialog pd;
        GridView gv;
        String result="";
        try {
            URL movieURL=new URL(BASE_URL);
            urlConnection=movieURL.openConnection ();
            bufferedReader =new BufferedReader ( new InputStreamReader ( urlConnection.getInputStream () ) );
            StringBuffer stringBuffer= new StringBuffer (  );

            String line;
            while((line=bufferedReader.readLine ())!=null){
                stringBuffer.append ( line );
            }
            //  return stringBuffer.toString ();
        } catch (IOException e) {
            e.printStackTrace ();
            return  null;
        }
        return result;
    }
    //check connectivity
    public static Boolean status ( Context context ) {
        ConnectivityManager manager = (ConnectivityManager) context.getSystemService ( Context.CONNECTIVITY_SERVICE );
        NetworkInfo netInfo = manager.getActiveNetworkInfo ();
        return netInfo != null && netInfo.isConnected ();
    }
    private  void parseJson ( String data  ) {
        try {
            JSONObject obj = new JSONObject ( data );
            JSONArray array = obj.optJSONArray ( "results" );
            Movie item;
            for (int i = 0; i < array.length (); i++) {
                JSONObject json = array.getJSONObject ( i );
                String title=json.optString ( "title" );
                String date=json.optString ( "release_date" );
                String overview=json.optString ( "overview" );
                String vote_average=json.optString ( "vote_average" );
                String poster_path=json.optString ( "poster_path" );
                item=new Movie(  );
                //set movie details
                item.setTitle ( title );
                item.setOverview ( overview );
                item.setVote_average ( vote_average );
                item.setPosterPath ( poster_path );
                item.setDate ( date );
                JSONArray jsonArray=array.getJSONArray ( Integer.parseInt ( "adult" ) );
                if(null!= jsonArray && jsonArray.length ()>0){
                    JSONObject jsonObject=jsonArray.getJSONObject ( 0 );
                    if(jsonObject!=null)
                        item.setPosterPath ( jsonObject.getString ( "url" ) );
                }
               /* movie.setTitle ( json.getString ( "title" ) );
                movie.setDate ( json.getString ( "release_date" ) );
                movie.setVote_average ( json.getString ( "vote_average" ) );
                movie.setOverview ( json.getString ( "overview" ) );
                movie.setPosterPath ( json.getString ( "poster_path" ) );
                //  movie.setPopularity ( json.getDouble ( "popularity" ) );*/



               //add movie to m ArrayList
                mGridData.add (item);
               // mGridView.add ( item );
            }
        } catch (JSONException e) {
            e.printStackTrace ();
            Log.e ( TAG , "Error occurred during JSON Parse" , e );
        }
    }
/*
    @Override
    protected Void doInBackground ( Void... voids ) {
        return null;
    }
    @Override
    protected void onPreExecute () {
        super.onPreExecute ();
        pd=new ProgressDialog (  )
    }
    @Override
    protected void onPostExecute ( Void aVoid ) {
        super.onPostExecute ( aVoid );
    }*/
}