package com.example.kupal.testapp5; //change the package name to your project's package name

import android.content.Context;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.Map;

public class MyFirebaseRecylerAdapter extends FirebaseRecyclerAdapter<MovieX, MyFirebaseRecylerAdapter.MovieViewHolder> {

    private Context mContext;

    private static RecyclerItemClickListener mItemClickListener;
    private Context context;
    private int lastposition = -1;
    int layoutType;
    List<Map<String, ?>> mData;
    public boolean check = false;
    private static PopupMenu popupMenu;

    public interface RecyclerItemClickListener{
        public void onItemClick(View view, int position);
        //public void onItemLongClick(View view, int position);
        //public void onItemCheckBoxSelect(int position, boolean isChecked);
        public void onOverFlowButtonClick(View view, int position);
    }

    public void setOnItemClickListener(final RecyclerItemClickListener mItemClickLisn){
        mItemClickListener = mItemClickLisn;
    }


    public MyFirebaseRecylerAdapter(Class<MovieX> modelClass, int modelLayout,
                                    Class<MovieViewHolder> holder, DatabaseReference ref, Context context) {
        super(modelClass, modelLayout, holder, ref);
        this.mContext = context;
    }

    @Override
    protected void populateViewHolder(MovieViewHolder movieViewHolder, MovieX movie, int i) {

        //TODO: Populate viewHolder by setting the movie attributes to cardview fields
        if(movieViewHolder.movieName!=null) {
            movieViewHolder.movieName.setText(movie.getName());
        }
        if(movieViewHolder.movieDesc!=null){
            movieViewHolder.movieDesc.setText(movie.getDescription());
        }
        if(movieViewHolder.movieImage!=null){
            Picasso.with(mContext).load(movie.getUrl()).into(movieViewHolder.movieImage);
        }
        if(movieViewHolder.rating!=null){
            movieViewHolder.rating.setText(movie.getRating()+"/10");
        }
    }

    @Override
    public MovieViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View newView;
        newView = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_item_view, parent, false);
        return new MovieViewHolder(newView);
    }


    //TODO: Populate ViewHolder and add listeners.
    public static class MovieViewHolder extends RecyclerView.ViewHolder {
        public ImageView movieImage;
        public TextView movieName;
        public TextView movieDesc;
        public TextView rating;
        public ImageView overflow;

        public MovieViewHolder(View v) {
            super(v);
            movieImage = (ImageView) itemView.findViewById(R.id.movieImage);
            movieName = (TextView) itemView.findViewById(R.id.movieN);
            movieDesc = (TextView) itemView.findViewById(R.id.movieD);
            rating = (TextView) itemView.findViewById(R.id.rat);
            overflow = (ImageView) itemView.findViewById(R.id.overflow);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mItemClickListener != null) {
                        mItemClickListener.onItemClick(v, getAdapterPosition());
                    }
                }
            });

            if (overflow != null) {
                overflow.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mItemClickListener != null)
                            mItemClickListener.onOverFlowButtonClick(v, getPosition());
                    }
                });
            }
        }
    }
}
//<------------------------------------------End--------------------------------------------------->