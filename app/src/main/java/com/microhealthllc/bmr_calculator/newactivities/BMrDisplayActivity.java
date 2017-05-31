package com.microhealthllc.bmr_calculator.newactivities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.NavUtils;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.microhealthllc.bmr_calculator.About;
import com.microhealthllc.bmr_calculator.LogActivity;
import com.microhealthllc.bmr_calculator.R;
import com.microhealthllc.bmr_calculator.fragment.BMrDisplayFragment;
import com.microhealthllc.bmr_calculator.helper.PreferencesHelper;
import com.microhealthllc.bmr_calculator.model.Player;

public class BMrDisplayActivity extends AppCompatActivity {

    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerLayout mDrawerLayout;
    private NavigationView mNavigationView;
    private static final String EXTRA_PLAYER = "player";
    Player player;

    public static void start(Activity activity, Player player, ActivityOptionsCompat options) {
        Intent starter = getStartIntent(activity, player);
        ActivityCompat.startActivity(activity, starter, options.toBundle());
    }


    public static void start(Context context, Player player) {
        Intent starter = getStartIntent(context, player);
        context.startActivity(starter);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            if (mDrawerLayout.isDrawerOpen(mNavigationView)) {
                mDrawerLayout.closeDrawer(mNavigationView);
            } else {
                mDrawerLayout.openDrawer(mNavigationView);
            }
           // NavUtils.navigateUpFromSameTask(this);
        }
        return super.onOptionsItemSelected(item);
    }


    private void setUpNavigationDrawer() {

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mNavigationView = (NavigationView) findViewById(R.id.navigation_view);
        mNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                menuItem.setChecked(true);
                switch (menuItem.getItemId()) {

                    case R.id.edit:
                        startActivity(new Intent(BMrDisplayActivity.this, SignInActivity.class));
                        break;
                    case R.id.log:
                        startActivity(new Intent(BMrDisplayActivity.this, LogActivity.class));

                        break;
                    case R.id.about:
                        //   mCurrentSelectedPosition = 3;

                        startActivity(new Intent(BMrDisplayActivity.this, About.class));
                        break;


                }

                // setTabs(mCurrentSelectedPosition + 1);
                mDrawerLayout.closeDrawer(mNavigationView);
                return true;
            }
        });

        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.drawer_open, R.string.drawer_close) {
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);

                invalidateOptionsMenu();
            }

            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);

                invalidateOptionsMenu();
            }
        };

        mDrawerToggle.setDrawerIndicatorEnabled(true);
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        mDrawerToggle.syncState();

    }


    @NonNull
    static Intent getStartIntent(Context context, Player player) {
        Intent starter = new Intent(context, BMrDisplayActivity.class);
        starter.putExtra(EXTRA_PLAYER, player);
        return starter;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bmr_display);
      player  = getIntent().getParcelableExtra(EXTRA_PLAYER);
        if (!PreferencesHelper.isSignedIn(this)) {
            if (player == null) {
                player = PreferencesHelper.getPlayer(this);
            } else {
                PreferencesHelper.writeToPreferences(this, player);
            }
        }
        if(getSupportActionBar()!=null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setTitle("");

        }


        setUpNavigationDrawer();
        attachBMrDisplayFragment();
    }



    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    private void attachBMrDisplayFragment() {
        FragmentManager supportFragmentManager = getSupportFragmentManager();
        Fragment fragment = supportFragmentManager.findFragmentById(R.id.category_container);
        if (!(fragment instanceof BMrDisplayFragment)) {
            fragment = BMrDisplayFragment.newInstance();
        }
        supportFragmentManager.beginTransaction()
                .replace(R.id.category_container, fragment)
                .commit();

    }
}
