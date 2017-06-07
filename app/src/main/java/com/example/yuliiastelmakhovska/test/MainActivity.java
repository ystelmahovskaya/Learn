package com.example.yuliiastelmakhovska.test;


import android.content.Intent;
import android.content.SharedPreferences;
import android.databinding.ObservableArrayList;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.facebook.login.LoginManager;

import java.text.ParseException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    public static String user_id;
    public static String ip="10.8.1.244:8000";
    public static int level=1;
    ScheduledExecutorService scheduler;
    Content content;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent= getIntent();
        user_id=intent.getStringExtra("user_id");
        PreferenceManager.setDefaultValues(this, R.xml.pref_general, false);
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        level=Integer.parseInt(sharedPrefs.getString("level_list","1"));
        content=new Content(getApplicationContext());
        content.sendRequest();
        scheduler = Executors.newSingleThreadScheduledExecutor();
        autoUpdateTimer(15);

        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
           // super.onBackPressed();
            moveTaskToBack(true);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
       

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        displaySelectedScreen(id);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void displaySelectedScreen(int id){
        Fragment fragment= null;
        ContentRepo repo= new ContentRepo(getApplicationContext());
        switch (id){
            case R.id.nav_camera:
                ObservableArrayList<Chapter> words= new ObservableArrayList<>();
                try {
                    words= repo.getWordsChapters(level);
                    Log.i("nav_camera",""+words.size());
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                fragment=new ChaptersList(words);
                break;
            case R.id.nav_gallery:
                fragment=new MyDictionary();

                break;
            case R.id.nav_slideshow:

                ObservableArrayList<Chapter> videos= new ObservableArrayList<>();
                try {
                     videos= repo.getVideoChapters(level);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                fragment=new ChaptersList(videos);
                break;
            case R.id.nav_share:
                ObservableArrayList<Chapter> quiz= new ObservableArrayList<>();
                try {
                    quiz=repo.getQuizChapters(level);



                } catch (ParseException e) {
                    e.printStackTrace();
                }
                fragment=new ChaptersList(quiz);

                break;
            case R.id.statistics:
                fragment=new Statistics();
                break;
            case R.id.nav_send:
                LoginManager.getInstance().logOut();
                finish();
                break;
            case R.id.settings:
                Intent settingsIntent = new Intent(this, SettingsActivity.class);

                settingsIntent.putExtra( SettingsActivity.EXTRA_SHOW_FRAGMENT, SettingsActivity.GeneralPreferenceFragment.class.getName() );

                settingsIntent.putExtra( SettingsActivity.EXTRA_NO_HEADERS, true );

                this.startActivity(settingsIntent);

                break;

        }
        if(fragment!=null){
            FragmentTransaction fragmentTransaction= getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.content_main, fragment).commit();
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
    }

    public void autoUpdateTimer(int time){
        scheduler.isShutdown();
        scheduler.scheduleAtFixedRate
                (new Runnable() {
                    @Override
                    public void run() {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                content.sendRequest();
                            }
                        });

                    }
                }, 0, time, TimeUnit.SECONDS);

    }


}
