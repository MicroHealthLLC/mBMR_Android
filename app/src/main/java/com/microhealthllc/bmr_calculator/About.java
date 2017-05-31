package com.microhealthllc.bmr_calculator;

import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.microhealthllc.bmr_calculator.DB.DBHandler;

import com.microhealthllc.bmr_calculator.floatbutton.FloatingActionButton;
import com.microhealthllc.bmr_calculator.floatbutton.FloatingActionMenu;
import com.microhealthllc.bmr_calculator.newactivities.SignInActivity;
import com.yarolegovich.lovelydialog.LovelyStandardDialog;

import butterknife.ButterKnife;

/**
 * Created by ubuntuadmin on 3/24/17.
 */

public class About  extends AppCompatActivity {

    private FloatingActionMenu menumainred;
    private FloatingActionButton enteredit_fab;
    private FloatingActionButton weight_graphfab;
    private FloatingActionButton bmi_logsfab;
    DBHandler db;
    private RecyclerView recyclerView;
    private LogListAdapter mAdapter;
    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerLayout mDrawerLayout;
    private NavigationView mNavigationView;
    Button twitterview;
    Button websitetxt;
    Button privacypolicy;
    Button termsofUse;
    TextView Lincenceagreement;
            TextView rateview;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.about);
        db = new DBHandler(this);

         twitterview = (Button) findViewById(R.id.followus);
        websitetxt =(Button) findViewById(R.id.webaddress);
        privacypolicy =(Button) findViewById(R.id.privacyp);
        termsofUse = (Button) findViewById(R.id.termsofuse);
       // twitterview.setMovementMethod(LinkMovementMethod.getInstance());

        twitterview.setText("@microhealthtalk");
        twitterview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                        gotoURl("https://twitter.com/MicroHealthTalk");
            }
        });
        websitetxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoURl("https://www.microhealthllc.com/");
            }
        });
                termsofUse.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        gotoURl("https://www.microhealthllc.com/about/terms-of-use/");

                    }
                });
                  privacypolicy.setOnClickListener(new View.OnClickListener() {
                      @Override
                      public void onClick(View v) {

                          gotoURl("https://www.microhealthllc.com/about/privacy-policy/");

                      }
                  });


     //   twitterview.setClickable(true);
    //    websitetxt =(TextView) findViewById(R.id.websiteurl);
     //   websitetxt.setClickable(true);
     //   websitetxt.setText(Html.fromHtml(website));

     //   twitterview.setText(Html.fromHtml(twiteracc));
       // Linkify.addLinks(twitterview,Linkify.ALL);
        ButterKnife.bind(this);
        if(getSupportActionBar()!=null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setTitle("");

        }
        setUpNavigationDrawer();




    }

    private void gotoURl(String url){

        Uri uriUrl = Uri.parse(url);
        Intent launchBrowser = new Intent(Intent.ACTION_VIEW, uriUrl);
        startActivity(launchBrowser);

    }


    private void setupToolbar() {
        final ActionBar ab = getSupportActionBar();
        ab.setHomeAsUpIndicator(R.drawable.ic_menu);
        ab.setDisplayHomeAsUpEnabled(true);
    }


    @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(mNavigationView)) {
            mDrawerLayout.closeDrawer(mNavigationView);
        } else {
            super.onBackPressed();
        }
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
                        // mCurrentSelectedPosition = 0;
                        startActivity(new Intent(About.this, SignInActivity.class));
                        break;
                    case R.id.log:
                        Intent j = new Intent(About.this, LogActivity.class);
                        startActivity(j);
                        break;
                    case R.id.about:
                        //    mCurrentSelectedPosition = 2;

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

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
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



}
