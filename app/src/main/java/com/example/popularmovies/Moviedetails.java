package com.example.popularmovies;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import butterknife.BindView;
import butterknife.ButterKnife;

public class Moviedetails extends AppCompatActivity {
    public static  final String MOVIE_POSTER_BASE_URL="https://image.tmdb.org/t/p/w185";
    private static final String TAG = Moviedetails.class.getSimpleName();

    @BindView( R.id.plotIv )
    TextView plot;
    @BindView ( R.id.titleIv )
    TextView title;
    @BindView ( R.id.imageIv )
    ImageView poster;
    @BindView ( R.id.voteIv )
    TextView vote;
    @BindView ( R.id.dateIv )
    TextView release_date;

    @Override
    protected void onCreate ( Bundle savedInstanceState ) {
        super.onCreate ( savedInstanceState );
        setContentView ( R.layout.detail);
        ButterKnife.bind ( this );
        Intent intent = getIntent ();
        RetroPhoto mIntent = (RetroPhoto) intent.getSerializableExtra ( "detail" );

        this.plot=  findViewById ( R.id.plotIv );
        this.title=  findViewById ( R.id.titleIv );
        this.poster= findViewById ( R.id.imageIv );
        this.release_date=  findViewById ( R.id.dateIv );
        this.vote=  findViewById ( R.id.voteIv );

        poster.setImageResource ( intent.getIntExtra ( "image",0 ) );
        display ( mIntent );
    }
    @Override
    public boolean onCreateOptionsMenu ( Menu menu ) {
        getMenuInflater ().inflate ( R.menu.menu, menu );
        return true;
    }
    @SuppressLint("StringFormatInvalid")
    public void display( RetroPhoto m){
        // Picasso.with(this).load(m.getPoster ()).into(poster);
        Picasso.get()
                .load( String.valueOf ( poster ) )
                .placeholder(R.mipmap.ic_launcher)
                .into((ImageView) findViewById ( R.id.imageIv ));
        title.setText ( m.getTitle () );
        vote.setText ( m.getVote_average () );
        plot.setText ( m.getOverview () );

        SimpleDateFormat dateFormat = new SimpleDateFormat ( "dd MMM, yyyy" );
        SimpleDateFormat dateInput = new SimpleDateFormat ( "yyyy-MM-dd" );
        String date;
        try {
            date = dateFormat.format ( dateInput.parse ( m.getDate ()) );
        } catch (ParseException e) {
            e.printStackTrace ();
            date = m.getDate ();
        }
        release_date.setText ( m.getDate () );


    }
}
