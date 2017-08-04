package com.example.kupal.testapp5;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import java.util.HashMap;

//import android.text.ClipboardManager;

public class RecyclerView extends AppCompatActivity implements RecyclerViewFragment.OnCardItemClickedListener{

    //<------------------------------------ Data Members --------------------------------->

    private Fragment currentFragment;
    Toolbar recyclerToolBar;
    ActionBar recyclerActionBar;
    private TextView textView;

    //<------------------------------------ Starting point of the activity --------------------------------->

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycler_view);

        recyclerToolBar = (Toolbar) findViewById(R.id.toolBarRecycler);
        setSupportActionBar(recyclerToolBar);
        recyclerToolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        recyclerActionBar = getSupportActionBar();
        recyclerActionBar.setDisplayHomeAsUpEnabled(true);
        recyclerActionBar.setHomeButtonEnabled(true);
        textView = (TextView) findViewById(R.id.toolbarText);
        textView.setTextIsSelectable(true);

        if(savedInstanceState!=null){
            if(getSupportFragmentManager().getFragment(savedInstanceState,"recycle")!=null){
                currentFragment = getSupportFragmentManager().getFragment(savedInstanceState,"recycle");
            }else{
                currentFragment = RecyclerViewFragment.newInstance("","");
            }
        }else{
            currentFragment = RecyclerViewFragment.newInstance("","");
        }
        getSupportFragmentManager().beginTransaction().replace(R.id.recyclerView2,currentFragment).commit();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if(currentFragment.isAdded()){
            getSupportFragmentManager().putFragment(outState,"recycle",currentFragment);
        }
    }

    //<------------------------------------ Add text to textView --------------------------------->

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        textView.setText("MovieTitle");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCardItemClicked(HashMap<String,?> m) {
        movie details = movie.newInstance(m);
        getSupportFragmentManager().beginTransaction().replace(R.id.recyclerView2,details).addToBackStack(null).commit();

    }
}


