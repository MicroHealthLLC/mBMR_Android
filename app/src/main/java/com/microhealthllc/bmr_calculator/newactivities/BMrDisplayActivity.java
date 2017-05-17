package com.microhealthllc.bmr_calculator.newactivities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.microhealthllc.bmr_calculator.R;
import com.microhealthllc.bmr_calculator.fragment.BMrDisplayFragment;
import com.microhealthllc.bmr_calculator.helper.PreferencesHelper;
import com.microhealthllc.bmr_calculator.model.Player;

public class BMrDisplayActivity extends AppCompatActivity {



    private static final String EXTRA_PLAYER = "player";

    public static void start(Activity activity, Player player, ActivityOptionsCompat options) {
        Intent starter = getStartIntent(activity, player);
        ActivityCompat.startActivity(activity, starter, options.toBundle());
    }


    public static void start(Context context, Player player) {
        Intent starter = getStartIntent(context, player);
        context.startActivity(starter);
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
        Player player = getIntent().getParcelableExtra(EXTRA_PLAYER);
        if (!PreferencesHelper.isSignedIn(this)) {
            if (player == null) {
                player = PreferencesHelper.getPlayer(this);
            } else {
                PreferencesHelper.writeToPreferences(this, player);
            }
        }

        attachBMrDisplayFragment();
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
