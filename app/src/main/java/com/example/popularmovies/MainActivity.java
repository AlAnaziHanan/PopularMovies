package com.example.popularmovies;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.AsyncTaskLoader;
import androidx.loader.content.Loader;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListAdapter;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends  AppCompatActivity implements LoaderManager.LoaderCallbacks<RetroPhoto> {

    private CustomAdapter myAdapter = new CustomAdapter (  new ArrayList<RetroPhoto> ( ),MainActivity.this);
    private RecyclerView recyclerView;
    ProgressDialog progressDialog;
    GridView myGrid;
    List<RetroPhoto> resultMovie;
    @BindView( R.id.bar )
    ProgressBar pbar;
    @BindView( R.id.moviesGrid )
    RecyclerView movieGrid;
    @BindView ( R.id.moviesGrid )

    public static final int SEARCH_LOADER = 22;
    //Place key
    public static final String URL_EXTRA = "https://api.themoviedb.org/3/movie/550?api_key=";

    private ArrayList<RetroPhoto> popularList;
    private ArrayList<RetroPhoto> topRatedList;

    private String popURL;
    private String rateURL;
    @Override
    protected void onCreate ( Bundle savedInstanceState ) {
        super.onCreate ( savedInstanceState );
        setContentView ( R.layout.activity_main );
        myGrid = findViewById ( R.id.moviesGrid );

        getSupportLoaderManager ().initLoader ( SEARCH_LOADER,null,this );

        progressDialog = new ProgressDialog ( MainActivity.this );
        progressDialog.show ();


        GetDataService service = RetrofitClientInstance.getRetrofitInstance ().create ( GetDataService.class );

        Call<List<RetroPhoto>> call = service.getAllPhotos ();
        call.enqueue (
                new Callback<List<RetroPhoto>> () {
                    @Override
                    public void onResponse ( Call<List<RetroPhoto>> call , Response<List<RetroPhoto>> response ) {
                        progressDialog.dismiss ();
                        generateList(response.body ());
                        myGrid.setAdapter ( (ListAdapter) myAdapter );
                        myGrid.setOnItemLongClickListener ( new AdapterView.OnItemLongClickListener () {
                            @Override
                            public boolean onItemLongClick ( AdapterView<?> adapterView , View view , int i , long l ) {
                                Intent intent = new Intent ( MainActivity.this, Moviedetails.class );
                                intent.putExtra ( "image", popularList.indexOf ( i ) );
                                startActivity ( intent );

                                return false;
                            }
                        } );

                        // ButterKnife.bind ( this );
                        pbar.setVisibility ( View.INVISIBLE );
                        new FetchMovies ().execute ();
                    }

                    @Override
                    public void onFailure ( Call<List<RetroPhoto>> call , Throwable throwable ) {
                        progressDialog.dismiss ();
                        Toast.makeText (MainActivity.this, "Error...Please Try again later.", Toast.LENGTH_SHORT).show ();

                    }
                });
    }

    //FetchMovies Class to get data from themoviedb url
    public class FetchMovies extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground ( Void... voids ) {
            //Place your API Key
            popURL = "http://api.themoviedb.org/3/discover/movie?sort_by=popularity.desc&api_key=";//+key;
            rateURL = "http://api.themoviedb.org/3/discover/movie?sort_by=vote_average.desc&api_key=";//+key;
            popularList = new ArrayList<> (  );
            topRatedList = new ArrayList<> (  );
            try{
                if(NetworkUtils.status ( MainActivity.this )){
                    //get popular, top rated movies
                    popularList = NetworkUtils.fetchData ( popURL );
                    topRatedList = NetworkUtils.fetchData ( rateURL );
                }
                else{
                    Toast.makeText (MainActivity.this, "No Internet Connection", Toast.LENGTH_LONG).show ();
                }
            }
            catch(IOException e){
                e.printStackTrace ();
            }
            return null;
        }

        @Override
        protected void onPreExecute () {
            super.onPreExecute ();
            pbar.setVisibility( View.VISIBLE);
        }

        @Override
        protected void onPostExecute ( Void aVoid ) {
            super.onPostExecute ( aVoid );
            pbar.setVisibility(View.INVISIBLE);
            myAdapter = new CustomAdapter (  popularList,MainActivity.this);
            myGrid.setAdapter( (ListAdapter) myAdapter );
        }
    }

    private void generateList( List<RetroPhoto> photoList){
        recyclerView = findViewById ( R.id.my_recycler_view );
        myAdapter = new CustomAdapter ( photoList, this );
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager ( MainActivity.this,3 );
        recyclerView.setLayoutManager ( layoutManager );
        recyclerView.setHasFixedSize ( true );
        recyclerView.setAdapter ( myAdapter );
    }

    @Override
    public Loader<RetroPhoto> onCreateLoader ( int id , @Nullable final Bundle args ) {

        return new AsyncTaskLoader<RetroPhoto> (this) {
            RetroPhoto resultHttp;
            @Override
            public RetroPhoto loadInBackground () {

                String url = args.getString ( URL_EXTRA );
                if (url == null && "".equals ( url )) {
                    return null;
                }
                try {
                    resultMovie = NetworkUtils.fetchData ( url);
                } catch (IOException e) {
                    e.printStackTrace ();
                }
                return (RetroPhoto) resultMovie;
            }
            @Override
            protected void onStartLoading () {
                if(resultHttp!=null){
                    deliverResult ( resultHttp );
                }else{
                    forceLoad ();
                }
            }
            @Override
            public void deliverResult ( RetroPhoto data ) {
                resultHttp=data;
                super.deliverResult ( data );
            }
        };
    }

    @Override
    public void onLoadFinished ( @NonNull Loader<RetroPhoto> loader , RetroPhoto data ) {
    }

    @Override
    public void onLoaderReset ( @NonNull Loader<RetroPhoto> loader ) {
    }

    private void makeQuery(String url){
        Bundle queryB = new Bundle (  );
        queryB.putString ( URL_EXTRA,url );
        LoaderManager loaderManager = getSupportLoaderManager ();

        Loader<RetroPhoto> loader = loaderManager.getLoader ( SEARCH_LOADER );
        if(loader==null){
            loaderManager.initLoader ( SEARCH_LOADER, queryB, this );
        }else{
            loaderManager.restartLoader ( SEARCH_LOADER,queryB,this );
        }
        //call:: makeQuery("https://api.themoviedb.org/3/movie/550?api_key=a1929f608371156c06e3be63aca37892");
    }

    @Override
    public boolean onCreateOptionsMenu ( Menu menu ) {
        getMenuInflater ().inflate ( R.menu.menu, menu );
        return true;
    }

    @Override
    public boolean onOptionsItemSelected ( @NonNull MenuItem item ) {
        int id = item.getItemId ();
        if(id==R.id.popularMovies){
            reorderList(popularList);
        }
        if(id==R.id.topRatedMovies){
            reorderList(topRatedList);
        }

        return super.onOptionsItemSelected ( item );
    }
    private void reorderList(ArrayList<RetroPhoto> list){
        CustomAdapter adapter = new CustomAdapter (  list, MainActivity.this );

        myGrid.invalidate();

        myGrid.setAdapter( (ListAdapter) adapter );
    }
}