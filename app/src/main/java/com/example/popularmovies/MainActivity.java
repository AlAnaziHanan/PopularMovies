package com.example.popularmovies;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends  AppCompatActivity  {
//implements LoaderManager.LoaderCallbacks<RetroPhoto>

    private GridViewAdapter adapter;
    private GridView mGridView;
    ProgressBar progressBar;

    private void populateGV (List<Movie> movie){

        mGridView =findViewById ( R.id.moviesGrid );
        adapter=new GridViewAdapter ( this, movie.size (), movie);
        mGridView.setAdapter ( adapter );

    }

    //Place key
    private List<Movie> popularList;
    private List<Movie> topRatedList;
    public static final String BASE_URL = "https://api.themoviedb.org/3/movie/550?api_key=";
    public static final String POPURL="http://api.themoviedb.org/3/discover/movie?sort_by=popularity.desc&api_key=";//+key;;
    public static final String RATEURL="http://api.themoviedb.org/3/discover/movie?sort_by=vote_average.desc&api_key=";//+key;;
    @Override
    protected void onCreate ( Bundle savedInstanceState ) {
        super.onCreate ( savedInstanceState );
        setContentView ( R.layout.activity_main );


        final ProgressBar progressBar=findViewById ( R.id.bar );
        progressBar.setIndeterminate ( true );

        progressBar.setVisibility ( View.VISIBLE );

        GetDataService dataService= RetrofitClientInstance.getRetrofitInstance (BASE_URL).create ( GetDataService.class );

        Call<List<Movie>> call = dataService.getAllPhotos ();

        call.enqueue ( new Callback<List<Movie>> () {
            @Override
            public void onResponse ( Call<List<Movie>> call , Response<List<Movie>> response ) {
                progressBar.setVisibility ( View.GONE );
                populateGV(response.body ());

                mGridView.setOnItemLongClickListener ( new AdapterView.OnItemLongClickListener () {
                    @Override
                    public boolean onItemLongClick ( AdapterView<?> adapterView , View view , int i , long l ) {
                        Intent intent = new Intent ( MainActivity.this, Moviedetails.class );
                        intent.putExtra ( "my_recycler_view", popularList.indexOf ( i ) );
                        startActivity ( intent );
                        return false;
                    }

                } );

            }

            @Override
            public void onFailure ( Call<List<Movie>> call , Throwable throwable ) {
                progressBar.setVisibility ( View.GONE );
                Toast.makeText ( MainActivity.this, throwable.getMessage (), Toast.LENGTH_LONG ).show ();
            }
        }  );


   /* private void reorderList(List<Movie> list){
        GridViewAdapter adapter = new GridViewAdapter (  MainActivity.this, popularList.size (),list);

        mGridView.invalidate();

        mGridView.setAdapter( (ListAdapter) adapter );
    }*/
    }

}