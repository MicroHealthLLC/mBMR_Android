/*
 * Copyright 2015 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.microhealthllc.bmr_calculator.helper;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.microhealthllc.bmr_calculator.model.Avatar;
import com.microhealthllc.bmr_calculator.model.Player;


/**
 * Easy storage and retrieval of preferences.
 */
public class PreferencesHelper {

    private static final String PLAYER_PREFERENCES = "playerPreferences";
    private static final String PREFERENCE_FIRST_NAME = PLAYER_PREFERENCES + ".firstName";
    private static final String PREFERENCE_AGE = PLAYER_PREFERENCES + ".age";
    private static final String PREFERENCE_HEIGHT = PLAYER_PREFERENCES + ".height";
    private static final String PREFERENCE_HEIGHT_IN = PLAYER_PREFERENCES + ".height_in";
    private static final String PREFERENCE_HEIGHT_FT = PLAYER_PREFERENCES + ".height_feet";
    private static final String PREFERENCE_WEIGHT = PLAYER_PREFERENCES + ".weight";
    private static final String PREFERENCE_IS_FEMALE = PLAYER_PREFERENCES + ".isfemale";
    private static final String PREFERENCE_AVATAR = PLAYER_PREFERENCES + ".avatar";
    private static final String PREFERENCE_ACTIVITY_POSITION = PLAYER_PREFERENCES+".activity_position";


    private PreferencesHelper() {
        //no instance
    }


    public static void writeToPreferences(Context context, Player player) {
        SharedPreferences.Editor editor = getEditor(context);
        editor.putString(PREFERENCE_FIRST_NAME, player.getFirstName());
        editor.putString(PREFERENCE_AGE, player.getAge());
      //  editor.putString(PREFERENCE_HEIGHT, player.getHeight());
        editor.putString(PREFERENCE_WEIGHT, player.getWeight());
        editor.putBoolean(PREFERENCE_IS_FEMALE, player.getIsfemale());
        editor.putString(PREFERENCE_AVATAR, player.getAvatar().name());
        editor.putInt(PREFERENCE_ACTIVITY_POSITION, player.getActivitiy_level_position());
        editor.putString(PREFERENCE_HEIGHT_FT, player.getHeight_feets());
        editor.putString(PREFERENCE_HEIGHT_IN, player.getHeight_inch());
        editor.apply();
    }

    /**

     *
     * @param context The Context which to obtain the SharedPreferences from.
     * @return A previously saved player or <code>null</code> if none was saved previously.
     */
    public static Player getPlayer(Context context) {
        SharedPreferences preferences = getSharedPreferences(context);
        final String firstName = preferences.getString(PREFERENCE_FIRST_NAME, null);
        final String age = preferences.getString(PREFERENCE_AGE, null);
        //final String height = preferences.getString(PREFERENCE_HEIGHT, null);
        final String weight = preferences.getString(PREFERENCE_WEIGHT, null);
        final boolean isfemale = preferences.getBoolean(PREFERENCE_IS_FEMALE, false);
        final String avatarPreference = preferences.getString(PREFERENCE_AVATAR, null);
        final String height_ft = preferences.getString(PREFERENCE_HEIGHT_FT,null);
        final String height_inch = preferences.getString(PREFERENCE_HEIGHT_IN,null);
        final int  activity_level_position = preferences.getInt(PREFERENCE_ACTIVITY_POSITION, 0);
        final Avatar avatar;
        if (null != avatarPreference) {
            avatar = Avatar.valueOf(avatarPreference);
        } else {
            avatar = null;
        }

        if (null == firstName || null == age || null == height_ft || null == avatar ) {
            return null;
        }
        return new Player(firstName,age,height_ft,height_inch, weight,isfemale,avatar,activity_level_position);
    }

    /**
     * Signs out a player by removing all it's data.
     *
     * @param context The context which to obtain the SharedPreferences from.
     */
    public static void signOut(Context context) {
        SharedPreferences.Editor editor = getEditor(context);
        editor.remove(PREFERENCE_FIRST_NAME);
        editor.remove(PREFERENCE_AGE);
        editor.remove(PREFERENCE_HEIGHT);
        editor.remove(PREFERENCE_WEIGHT);
        editor.remove(PREFERENCE_IS_FEMALE);
        editor.remove(PREFERENCE_AVATAR);
        editor.apply();
    }

    /**
     * Checks whether a player is currently signed in.
     *
     * @param context The context to check this in.
     * @return <code>true</code> if login data exists, else <code>false</code>.
     */
    public static boolean isSignedIn(Context context) {
        final SharedPreferences preferences = getSharedPreferences(context);
        return preferences.contains(PREFERENCE_FIRST_NAME) &&
                preferences.contains(PREFERENCE_AGE) &&
                preferences.contains(PREFERENCE_AVATAR);
    }

    /**
     * Checks whether the player's input data is valid.
     *
     * @param firstName   The player's first name to be examined.
     * @param age The player's last initial to be examined.
     * @return <code>true</code> if both strings are not null nor 0-length, else <code>false</code>.
     */
    public static boolean isInputDataValid(CharSequence firstName, CharSequence age, CharSequence height, CharSequence weight) {
        return !TextUtils.isEmpty(firstName) && !TextUtils.isEmpty(age) && !TextUtils.isEmpty(height)&& !TextUtils.isEmpty(weight) ;
    }

    private static SharedPreferences.Editor getEditor(Context context) {
        SharedPreferences preferences = getSharedPreferences(context);
        return preferences.edit();
    }

    private static SharedPreferences getSharedPreferences(Context context) {
        return context.getSharedPreferences(PLAYER_PREFERENCES, Context.MODE_PRIVATE);
    }
}
