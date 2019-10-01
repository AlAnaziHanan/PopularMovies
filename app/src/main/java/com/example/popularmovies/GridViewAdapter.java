package com.example.popularmovies;


import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

class GridViewAdapter extends ArrayAdapter<Movie> {

    private List<Movie> mGridData ;
    private Context context;

    public GridViewAdapter ( Context context , int resource , List<Movie> mGridData ) {
        super ( context , resource , mGridData );
        this.context = context;
        this.mGridData = mGridData;
    }
    @Override
    public long getItemId ( int i ) {
        return i;
    }


    @Override
    public View getView ( int i , View view ,  ViewGroup viewGroup ) {

        View row = view;
        ViewHolder holder;
        if (row == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater ();
            //row = inflater.inflate ( layoutResource , viewGroup , false );
            view= LayoutInflater.from ( context ).inflate ( R.layout.model,viewGroup,false );
            holder = new ViewHolder ();
            holder.imageView = view.findViewById ( R.id.imageIV );
            view.setTag ( holder );

        } else {

            holder = (ViewHolder) view.getTag ();
        }
        Movie item = mGridData.get ( i );
        Picasso.get ( ).load ( item.getPosterPath ()).into ( holder.imageView );
        return view;
       /* ImageView poster = view.findViewById ( R.id.imageIV );

        final Movie thisMovie=movie.get ( i );
        if(thisMovie.getPosterPath ()!=null && thisMovie.getPosterPath ().length ()>0){
            Picasso.get ().load ( thisMovie.getPosterPath ().placeholder(R.drawable.placeholder_foreground).into(poster) );
        }
        else{
            Toast.makeText ( context, "Empty image URL", Toast.LENGTH_LONG ).show ();
            Picasso.get ().load ( R.drawable.placeholder_foreground ).into ( poster );
        }
        view.setOnClickListener ( new View.OnClickListener () {


            @Override
            public void onClick ( View view ) {
                Toast.makeText ( context, thisMovie.getTitle (),Toast.LENGTH_LONG ).show ();
            }
        });

        return view;
    }*/
    }

    //update grid data
    public void refreshGridData (ArrayList <Movie> mGridData) {
        this.mGridData = mGridData;
        notifyDataSetChanged ();
    }
    class ViewHolder {
        ImageView imageView;
    }
      /*
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
                                intent.putExtra ( "my_recycler_view", popularList.indexOf ( i ) );
                                startActivity ( intent );
                                return false;
                            }
                        } );

                        ButterKnife.bind ( myGrid );
                        pbar.setVisibility ( View.INVISIBLE );
                        new FetchMovies ().execute ();
                    }

                    @Override
                    public void onFailure ( Call<List<RetroPhoto>> call , Throwable throwable ) {
                        progressDialog.dismiss ();
                        Toast.makeText (MainActivity.this, "Error...Please Try again later.", Toast.LENGTH_SHORT).show ();

                    }
                });*/


//FetchMovies Class to get data from themoviedb url
   /* public class FetchMovies extends AsyncTask<Void, Void, Void> {


        @Override
        protected Void doInBackground ( Void... voids ) {
            //Place your API Key
            popURL = "http://api.themoviedb.org/3/discover/movie?sort_by=popularity.desc&api_key=";//+key;
            rateURL = "http://api.themoviedb.org/3/discover/movie?sort_by=vote_average.desc&api_key=";//+key;
            popularList = new ArrayList<> (  );
            topRatedList = new ArrayList<> (  );
            HttpURLConnection connection=null;
            BufferedReader reader=null;

            try{
                URL url = new URL(URL_EXTRA);
                connection=(HttpURLConnection) url.openConnection ();
                connection.connect ();

                InputStream stream=connection.getInputStream ();

                reader=new BufferedReader ( new InputStreamReader ( stream ) );
                StringBuffer buffer=new StringBuffer (  );
                String line ="";
                while((line=reader.readLine ())!=null){
                    buffer.append ( line+"\n ");
                    Log.d ("adult",">"+line);
                }

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
                    resultMovie = new FetchMovies ();
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
    }*/


}