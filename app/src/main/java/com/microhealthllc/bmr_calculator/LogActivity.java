package com.microhealthllc.bmr_calculator;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.microhealthllc.bmr_calculator.DB.BmiLogs;
import com.microhealthllc.bmr_calculator.DB.DBHandler;

import com.microhealthllc.bmr_calculator.floatbutton.FloatingActionMenu;
import com.microhealthllc.bmr_calculator.newactivities.BMrDisplayActivity;
import com.microhealthllc.bmr_calculator.newactivities.SignInActivity;
import com.yarolegovich.lovelydialog.LovelyStandardDialog;

import java.util.ArrayList;
import java.util.List;

public class LogActivity extends AppCompatActivity {
    private List<LogModel> logList = new ArrayList<>();
    private RecyclerView recyclerView;
    private LogListAdapter mAdapter;
    DBHandler db;
    private FloatingActionMenu menumainred;
    private FloatingActionButton home_fab;
    private FloatingActionButton delete_fab;
    private FloatingActionButton enteredit_fab;
    private FloatingActionButton bmi_logsfab;

    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerLayout mDrawerLayout;
    private NavigationView mNavigationView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loghistory);
       // Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
       // setSupportActionBar(toolbar);
        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setHomeButtonEnabled(true);

        db = new DBHandler(this);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        mAdapter = new LogListAdapter(logList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);
        delete_fab = (FloatingActionButton) findViewById(R.id.fab);
        delete_fab.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                new LovelyStandardDialog(LogActivity.this)
                        .setTopColorRes(R.color.accent)
                        .setButtonsColorRes(R.color.accent)

                        .setTitle("Warning!!")
                        .setMessage(" This will delete all stored data,This cannot be undone")
                        .setPositiveButton(android.R.string.ok, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Toast.makeText(LogActivity.this, "positive clicked", Toast.LENGTH_SHORT).show();
                                db.deleteEntry();
                               startActivity(new Intent(LogActivity.this, SignInActivity.class));


                            }
                        })
                        .setNegativeButton(android.R.string.no, null)
                        .show();
            }
        });

        prepareMovieData();
        setUpNavigationDrawer();
    }

    private void prepareMovieData() {
        LogModel loghistory ;


        try {
            // Reading all shops
            Log.d("Reading:", "Reading all Logs.");
            List<BmiLogs> logs = db.getAllShops();

            for (BmiLogs log : logs) {
                loghistory = new LogModel(log.getBmi(), log.getWeight(), log.getCalories_needed(),log.getDateTime());


                logList.add(loghistory);

// Writing shops to log
                Log.d("BMILO: : ", "weight:" + log.getWeight() + "  BMI:" + log.getBmi() + "  DateTime:" + log.getDateTime());

            }

        } catch (Exception e) {
            Log.i("Thisexcept", e.toString());
        }


        mAdapter.notifyDataSetChanged();
    }






    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }


    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            if (mDrawerLayout.isDrawerOpen(mNavigationView)) {
                mDrawerLayout.closeDrawer(mNavigationView);
            } else {
                mDrawerLayout.openDrawer(mNavigationView);
            }
        }
        return super.onOptionsItemSelected(item);
    }


    private void setUpNavigationDrawer() {
/*
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();

        try {
            assert actionBar != null;
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true);
            actionBar.setSubtitle(getString(R.string.subtitle));
            actionBar.setDisplayShowTitleEnabled(true);
        } catch (Exception ignored) {
        }
*/
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mNavigationView = (NavigationView) findViewById(R.id.navigation_view);
        mNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                menuItem.setChecked(true);
                switch (menuItem.getItemId()) {

                        // mCurrentSelectedPosition = 0;
                      //  startActivity(new Intent(LogActivity.this, BMrDisplayActivity.class));

                    case R.id.edit:
                        //  mCurrentSelectedPosition = 1;
                        startActivity(new Intent(LogActivity.this, SignInActivity.class));

                        break;
                    case R.id.log:
                        //    mCurrentSelectedPosition = 2;

                        break;
                    case R.id.about:
                        //   mCurrentSelectedPosition = 3;
                        startActivity(new Intent(LogActivity.this, About.class));


                        break;

                  //  case R.id.navigation_item_6:


                }

                // setTabs(mCurrentSelectedPosition + 1);
                mDrawerLayout.closeDrawer(mNavigationView);
                return true;
            }
        });

        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.drawer_open, R.string.drawer_close) {
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                //getSupportActionBar().setTitle(getString(R.string.drawer_opened));
                invalidateOptionsMenu();
            }

            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                //getSupportActionBar().setTitle(mActivityTitle);
                invalidateOptionsMenu();
            }
        };

        mDrawerToggle.setDrawerIndicatorEnabled(true);
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        mDrawerToggle.syncState();

    }

}