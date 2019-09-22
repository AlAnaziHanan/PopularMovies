package com.example.popularmovies;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.OkHttp3Downloader;
import com.squareup.picasso.Picasso;

import java.util.List;

class CustomAdapter extends
        RecyclerView.Adapter<CustomAdapter.CustomViewHolder> {

    private List<RetroPhoto> dataList;
    private Context context;

    public CustomAdapter ( List<RetroPhoto> dataList , Context context ) {
        this.dataList = dataList;
        this.context = context;
    }

    @NonNull
    @Override
    public CustomViewHolder onCreateViewHolder ( @NonNull ViewGroup parent , int viewType ) {

        LayoutInflater layoutInflater = LayoutInflater.from ( parent.getContext () );
        View view = layoutInflater.inflate ( R.layout.activity_main, parent, false );
        return  new CustomViewHolder ( view );
    }

    @Override
    public void onBindViewHolder ( @NonNull CustomViewHolder holder , int position ) {
        holder.title.setText ( dataList.get(position).getTitle () );
        holder.overview.setText ( dataList.get(position).getOverview () );
        holder.date.setText ( dataList.get(position).getDate () );
        holder.vote.setText ( dataList.get(position).getVote_average () );

        Picasso.Builder builder = new Picasso.Builder ( context );
        builder.downloader ( new OkHttp3Downloader ( context ) );
        builder.build ().load ( dataList.get ( position ).getPosterPath () )
                .placeholder ( (R.drawable.ic_launcher_background) )
                .error ( R.drawable.ic_launcher_background )
                .into ( holder.image );
    }

    @Override
    public int getItemCount () {
        return dataList.size ();
    }

    class CustomViewHolder extends RecyclerView.ViewHolder{

        public final View view;
        TextView title, date, overview, vote;
        private ImageView image;
        CustomViewHolder( View itemView ){
            super(itemView);
            view=itemView;

            title = view.findViewById ( R.id.titleIv );
            date = view.findViewById ( R.id.dateIv );
            overview = view.findViewById ( R.id.plot );
            vote = view.findViewById ( R.id.voteIv );
            image = view.findViewById ( R.id.imageIv );
        }
    }
}