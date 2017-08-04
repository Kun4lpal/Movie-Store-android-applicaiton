package com.example.kupal.testapp5;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener,Front_frag.OnFragmentInteractionListener,Front_frag.OnButtonClickedListener,Front_frag.OnTask1ClickedListener, Front_frag.OnTask2ClickedListener{

    //<---------------------------------------Data members--------------------------------------------------->

    private Fragment currentFragment;
    NavigationView navigationView;
    DrawerLayout drawerLayout;
    //ActionBar homeActionBar;
    private Toolbar toolbar;

    //<------------------------------------Starting point of our Application --------------------------------->

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = (Toolbar) findViewById(R.id.toolbarm);
        setSupportActionBar(toolbar);

        navigationView = (NavigationView) findViewById(R.id.navigator);
        navigationView.setNavigationItemSelectedListener(this);

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer);
        ActionBarDrawerToggle toggler = new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.drawer_open, R.string.drawer_close){
            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }
        };

        drawerLayout.addDrawerListener(toggler);
        toggler.syncState();


        if(savedInstanceState!=null){
            if(getSupportFragmentManager().getFragment(savedInstanceState,"current")!=null){
                currentFragment = getSupportFragmentManager().getFragment(savedInstanceState,"current");
            }else{
                currentFragment = Front_frag.newInstance("","");
            }
        }else{
            currentFragment = Front_frag.newInstance("","");
        }
        getSupportFragmentManager().beginTransaction().replace(R.id.main_container,currentFragment).commit();
    }

//<---------------------------Somehow need this for interaction-------------------------------------------->

    @Override
    public void onFragmentInteraction(Uri uri) {

    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if(currentFragment.isAdded()){
            getSupportFragmentManager().putFragment(outState,"current",currentFragment);
        }
    }

    //<---------------------------------Button implementations inside the front fragment---------------------------------->

    @Override
    public void onButtonClicked(Bundle savedInstanceState) {
        currentFragment=AboutMe.newInstance();
        getSupportFragmentManager().beginTransaction().replace(R.id.main_container,currentFragment).addToBackStack(null).commit();
    }

    @Override
    public void onTask1ButtonClicked(Bundle savedInstanceState) {
        Intent intent = new Intent(this,RecyclerView.class);
        startActivity(intent);
    }

    @Override
    public void onTask2ButtonClicked(Bundle savedInstanceState) {
        Intent intent = new Intent(this,Task2.class);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_menu,menu);
        return  true;
    }

    //<---------------------------------OptionMenu inside the fragment----------------------------------------------->

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int res_id = item.getItemId();
        if(res_id == R.id.task1){
            Intent intent = new Intent(this,RecyclerView.class);
            startActivity(intent);
        }else if (res_id == R.id.task0){
            currentFragment=AboutMe.newInstance();
            getSupportFragmentManager().beginTransaction().replace(R.id.main_container,currentFragment).addToBackStack(null).commit();
            Toast.makeText(getApplicationContext(), "You selected Task 0 ", Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }

    //<--------------------------------Navigation Drawer with banned in front fragment------------------------------>

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch(id){
            case R.id.Logout:
            FirebaseAuth auth = FirebaseAuth.getInstance();
            auth.signOut();
            Intent intentlogin = new Intent(this,LoginActivity.class);
            startActivity(intentlogin);
                break;
            case R.id.task0:
                currentFragment=AboutMe.newInstance();
                getSupportFragmentManager().beginTransaction().replace(R.id.main_container,currentFragment).addToBackStack(null).commit();
                Toast.makeText(getApplicationContext(), "You selected Task 0 ", Toast.LENGTH_SHORT).show();
                break;
            case R.id.task1:
                Intent intent = new Intent(this,RecyclerView.class);
                startActivity(intent);
                break;
            case R.id.task2:
                Intent intent2 = new Intent(this,Task2.class);
                startActivity(intent2);
                break;
            default:
                getSupportFragmentManager().beginTransaction().replace(R.id.main_container,Front_frag.newInstance("","")).addToBackStack(null).commit();
                break;
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
}
//<-------------------------------------------------END--------------------------------------------------->