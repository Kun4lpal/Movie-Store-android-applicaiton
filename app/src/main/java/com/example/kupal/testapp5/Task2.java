package com.example.kupal.testapp5;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;

public class Task2 extends AppCompatActivity implements RecyclerViewFragment.OnCardItemClickedListener{

    private Fragment currentFragment;
    Toolbar recyclerToolBar;
    ActionBar recyclerActionBar;
    Toolbar toolBarAsAction;
    private TextView textView;
    private boolean toggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        toggle = false;
        setContentView(R.layout.activity_task2);
        toolBarAsAction = (Toolbar) findViewById(R.id.toolBarTask2);
        setSupportActionBar(toolBarAsAction);
        toolBarAsAction.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        recyclerActionBar = getSupportActionBar();
        recyclerActionBar.setDisplayHomeAsUpEnabled(true);
        recyclerActionBar.setHomeButtonEnabled(true);

        recyclerToolBar = (Toolbar) findViewById(R.id.toolBarR2);
        recyclerToolBar.inflateMenu(R.menu.recycler_view_menu_1_toolbar);
        toolBarAsAction.setNavigationIcon(R.drawable.close);
        toolBarAsAction.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!toggle) {
                    recyclerToolBar.setVisibility(View.GONE);
                    toggle = true;
                }else{
                    recyclerToolBar.setVisibility(View.VISIBLE);
                    toggle = false;
                }

            }
        });
        textView = (TextView) findViewById(R.id.task2text);
        textView.setTextIsSelectable(true);
        recyclerToolBar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {

            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int id = item.getItemId();
                switch (id) {
                    case R.id.sortbyYear:
                        Fragment f1 = getSupportFragmentManager().findFragmentById(R.id.recyclerV2);
                        if(f1 instanceof movie)
                            return false;
                        RecyclerViewFragment fragment = (RecyclerViewFragment)getSupportFragmentManager().findFragmentById(R.id.recyclerV2);
                        if(fragment instanceof RecyclerViewFragment) {
                            fragment.sortByYear();
                            return true;
                        }
                        return true;

                    case R.id.gridView:
                        Fragment f2 = getSupportFragmentManager().findFragmentById(R.id.recyclerV2);
                        if(f2 instanceof movie)
                            return false;
                        RecyclerViewFragment fragment1 = (RecyclerViewFragment)getSupportFragmentManager().findFragmentById(R.id.recyclerV2);
                        if(fragment1 instanceof RecyclerViewFragment) {
                            fragment1.changeToGridView();
                            return true;
                        }
                    default:
                        return false;
                }
            }
        });

        if(savedInstanceState!=null){
            if(getSupportFragmentManager().getFragment(savedInstanceState,"recycle")!=null){
                currentFragment = getSupportFragmentManager().getFragment(savedInstanceState,"recycle");
            }else{
                currentFragment = RecyclerViewFragment.newInstance("","");
            }
        }else{
            currentFragment = RecyclerViewFragment.newInstance("","");
        }
        getSupportFragmentManager().beginTransaction().replace(R.id.recyclerV2,currentFragment).commit();


    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if(currentFragment.isAdded()){
            getSupportFragmentManager().putFragment(outState,"recycle",currentFragment);
        }
    }

    @Override
    public void onCardItemClicked(HashMap<String, ?> mov) {
        Toast.makeText(this, "Click", Toast.LENGTH_SHORT).show();
        textView.setText(mov.get("name").toString());
        //getSupportFragmentManager().beginTransaction().replace(R.id.recyclerV2,movie.newInstance(mov)).addToBackStack(null).commit();
    }

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        int id = item.getItemId();
//        switch (id){
//            case R.id.onButton:
//                toolBarAsAction.setVisibility(View.VISIBLE);
//                return true;
//            default:
//                return false;
//        }
//    }
}
//<----------------------------------------------------End---------------------------------------------------------->