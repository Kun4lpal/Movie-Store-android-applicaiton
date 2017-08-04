package com.example.kupal.testapp5;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.ShareActionProvider;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.HashMap;

/**
 * A simple {@link Fragment} subclass.
 */
public class movie extends Fragment {

    //<------------------------------------Data members --------------------------------->

    HashMap<String,?> movie;
    TextView movieName;
    ImageView imgView;
    TextView desc;
    RatingBar rating;
    TextView stars;
    TextView textRating;
    TextView dirName;
    ShareActionProvider shareActionProvider;

    //<------------------------------------Constructor --------------------------------->

    public movie() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        movie = (HashMap<String, ?>) getArguments().getSerializable("movie");
        View view= inflater.inflate(R.layout.fragment_movie, container, false);
        movieName = (TextView) view.findViewById(R.id.movieName);
        movieName.setText((String)movie.get("name")+" ("+(String)movie.get("year")+")");

        imgView= (ImageView) view.findViewById(R.id.imgView3);
        Picasso.with(getActivity()).load((String) movie.get("url")).into(imgView);

        desc = (TextView) view.findViewById(R.id.desc);
        desc.setText((String) movie.get("description"));

        rating = (RatingBar) view.findViewById(R.id.ratingBar);
        //rating.setRating((Float) movie.get("rating"));

        String mRating = (String) movie.get("rating");
        float convRating = Float.valueOf(mRating)  ;
        rating.setRating(convRating/2.0f);

        textRating = (TextView) view.findViewById(R.id.textRating);
        //textRating.setText(""+(convRating*2)+"/10");

        stars = (TextView) view.findViewById(R.id.stars);
        stars.setText((String) movie.get("stars"));

        dirName = (TextView) view.findViewById(R.id.directorName);
        dirName.setText((String)movie.get("director"));
        return view;
    }

    //<------------------------------------Called when creating a new instance --------------------------------->

    public static movie newInstance(HashMap<String,?> item) {
        Bundle args = new Bundle();
        movie fragment = new movie();

        args.putSerializable("movie",item);
        fragment.setArguments(args);
        return fragment;
    }

    //<------------------------------------Option menu of the toolbar --------------------------------->

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        if(menu.findItem(R.id.shareAction)==null){
            inflater.inflate(R.menu.movie_fragment_action_provider,menu);
        }
        MenuItem share = menu.findItem(R.id.shareAction);
        shareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(share);

        //Create intent
        Intent intentShare = new Intent(Intent.ACTION_SEND);
        intentShare.setType("text/plain");
        intentShare.putExtra(Intent.EXTRA_TEXT,(String)movie.get("name")+ movie.get("description"));

        //set intent
        shareActionProvider.setShareIntent(intentShare);
        super.onCreateOptionsMenu(menu, inflater);
    }
}
