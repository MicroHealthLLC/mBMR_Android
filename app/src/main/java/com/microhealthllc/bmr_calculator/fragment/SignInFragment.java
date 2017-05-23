package com.microhealthllc.bmr_calculator.fragment;/*
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



import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v4.util.Pair;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.animation.FastOutSlowInInterpolator;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.transition.Transition;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.microhealthllc.bmr_calculator.R;
import com.microhealthllc.bmr_calculator.adapter.AvatarAdapter;
import com.microhealthllc.bmr_calculator.helper.ApiLevelHelper;
import com.microhealthllc.bmr_calculator.helper.PreferencesHelper;
import com.microhealthllc.bmr_calculator.helper.TransitionHelper;
import com.microhealthllc.bmr_calculator.model.Avatar;
import com.microhealthllc.bmr_calculator.model.Player;
import com.microhealthllc.bmr_calculator.newactivities.BMrDisplayActivity;
import com.microhealthllc.bmr_calculator.widget.TransitionListenerAdapter;


/**
 * Enable selection of an {@link Avatar} and user name.
 */
public class SignInFragment extends Fragment implements AdapterView.OnItemSelectedListener{

    private static final String ARG_EDIT = "EDIT";
    private static final String KEY_SELECTED_AVATAR_INDEX = "selectedAvatarIndex";
    private Player mPlayer;
    private EditText mFirstName;
    private EditText mAge;
    private EditText mWeight;
    private EditText mHeight_feet;
    private EditText mHeight_inches;
    private RadioGroup malefemale;
    Spinner spinner;
    public Avatar mSelectedAvatar;
    private View mSelectedAvatarView;
    private GridView mAvatarGrid;
    private FloatingActionButton mDoneFab;
    private int activity_position;
    String player_firstname;
    String player_age;
    String player_weight;
    String player_height;
    public boolean isfemale;

    private boolean edit;

    public static SignInFragment newInstance(boolean edit) {
        Bundle args = new Bundle();
        args.putBoolean(ARG_EDIT, edit);
        SignInFragment fragment = new SignInFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public static SignInFragment newInstance() {
        return new SignInFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        if (savedInstanceState != null) {
            final int savedAvatarIndex = savedInstanceState.getInt(KEY_SELECTED_AVATAR_INDEX);
            if (savedAvatarIndex != GridView.INVALID_POSITION) {
                mSelectedAvatar = Avatar.values()[savedAvatarIndex];
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        final View contentView = inflater.inflate(R.layout.fragment_sign_in, container, false);
        contentView.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom,
                                       int oldLeft, int oldTop, int oldRight, int oldBottom) {
                v.removeOnLayoutChangeListener(this);
                setUpGridView(getView());
            }

        });

        return contentView;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        if (mAvatarGrid != null) {
            outState.putInt(KEY_SELECTED_AVATAR_INDEX, mAvatarGrid.getCheckedItemPosition());
        } else {
            outState.putInt(KEY_SELECTED_AVATAR_INDEX, GridView.INVALID_POSITION);
        }
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
       assurePlayerInit();
       // assurePlayerInit();
        checkIsInEditMode();

        view.findViewById(R.id.content).setVisibility(View.VISIBLE);
        initContentViews(view);
        initContents();
        isInputEmpty();

        super.onViewCreated(view, savedInstanceState);

    }

    private void checkIsInEditMode() {
        final Bundle arguments = getArguments();
        //noinspection SimplifiableIfStatement
        if (arguments == null) {
            edit = false;
        } else {
            edit = arguments.getBoolean(ARG_EDIT, false);
        }
    }

    private void initContentViews(View view) {
        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                /* no-op */
              //  mDoneFab.setVisibility(getView().VISIBLE);
               // mDoneFab.show();
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // hiding the floating action button if text is empty
                if (s.length() == 0) {
                   mDoneFab.hide();
                }

            }

            @Override
            public void afterTextChanged(Editable s) {
                // showing the floating action button if avatar is selected and input data is valid
                if (isAvatarSelected() && isInputDataValid()) {
                    mDoneFab.show();
                }
            }
        };

        mFirstName = (EditText) view.findViewById(R.id.first_name);
        mFirstName.addTextChangedListener(textWatcher);
        mAge = (EditText) view.findViewById(R.id.last_initial);
        mAge.addTextChangedListener(textWatcher);
        mWeight =(EditText)view.findViewById(R.id.weight);
        mWeight.addTextChangedListener(textWatcher);
        mHeight_feet = (EditText)view.findViewById(R.id.feets);
        mHeight_feet.addTextChangedListener(textWatcher);
        mHeight_inches = (EditText)view.findViewById(R.id.inches);

        mDoneFab = (FloatingActionButton) view.findViewById(R.id.done);
        mDoneFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.done:
                        savePlayer(getActivity());
                        isInputEmpty();

                                if (null == mSelectedAvatarView) {
                                    performSignInWithTransition(mAvatarGrid.getChildAt(
                                            mSelectedAvatar.ordinal()));
                                } else {
                                    performSignInWithTransition(mSelectedAvatarView);
                                }

                        break;
                    default:
                        throw new UnsupportedOperationException(
                                "The onClick method has not been implemented for " + getResources()
                                        .getResourceEntryName(v.getId()));
                }
            }
        });

        spinner = (Spinner) getActivity().findViewById(R.id.spinner);
// Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.active_level, R.layout.spinnerlayout);
// Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(R.layout.spinnerlayout);
// Apply the adapter to the spinner
        spinner.setAdapter(adapter);
        malefemale = (RadioGroup) getActivity().findViewById(R.id.gender);
        malefemale.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                if (checkedId== R.id.male){
                    isfemale =false;
                }
                else {
                    isfemale =true;
                }
            }
        });
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                   activity_position= position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                activity_position = 0;

            }
        });

    }

    private void removeDoneFab(@Nullable Runnable endAction) {
        ViewCompat.animate(mDoneFab)
                .scaleX(0)
                .scaleY(0)
                .setInterpolator(new FastOutSlowInInterpolator())
                .withEndAction(endAction)
                .start();
         mDoneFab.setVisibility(getView().VISIBLE);


    }

    private void setUpGridView(View container) {

        mAvatarGrid = (GridView) container.findViewById(R.id.avatars);
        mAvatarGrid.setAdapter(new AvatarAdapter(getActivity()));
        mAvatarGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mSelectedAvatarView = view;
                mSelectedAvatar = Avatar.values()[position];
                // showing the floating action button if input data is valid
                Toast.makeText(getActivity(), "selected avatar pos: "+position, Toast.LENGTH_SHORT).show();
                if (isInputDataValid()) {
                    mDoneFab.show();
                }

            }
        });
        mAvatarGrid.setNumColumns(calculateSpanCount());
        if (mSelectedAvatar != null) {
            mAvatarGrid.setItemChecked(mSelectedAvatar.ordinal(), true);
        }
    }

    private void performSignInWithTransition(View v) {
        final Activity activity = getActivity();
        assurePlayerInit();
        if (v == null || ApiLevelHelper.isLowerThan(Build.VERSION_CODES.LOLLIPOP)) {
            // Don't run a transition if the passed view is null
           // savePlayer(getActivity());
       BMrDisplayActivity.start(activity, mPlayer);
         // activity.finish();
            return;
        }

        if (ApiLevelHelper.isAtLeast(Build.VERSION_CODES.LOLLIPOP)) {

            activity.getWindow().getSharedElementExitTransition().addListener(
                    new TransitionListenerAdapter() {
                        @Override
                        public void onTransitionEnd(Transition transition) {
                           // activity.finish();
                        }
                    });

            final Pair[] pairs = TransitionHelper.createSafeTransitionParticipants(activity, true,
                    new Pair<>(v, activity.getString(R.string.transition_avatar)));
            @SuppressWarnings("unchecked")
            ActivityOptionsCompat activityOptions = ActivityOptionsCompat
                    .makeSceneTransitionAnimation(activity, pairs);
           // savePlayer(getActivity());
          BMrDisplayActivity.start(activity, mPlayer, activityOptions);
        }
    }

    private void initContents() {
        assurePlayerInit();
        if (mPlayer != null) {
          mFirstName.setText(mPlayer.getFirstName());
            mAge.setText(mPlayer.getAge());
            mWeight.setText(mPlayer.getWeight());
            mHeight_feet.setText(mPlayer.getHeight_feets());
            mHeight_inches.setText(mPlayer.getHeight_inch());
            mSelectedAvatar = mPlayer.getAvatar();
        }
    }

    private void assurePlayerInit() {
        if (mPlayer == null) {
            mPlayer = PreferencesHelper.getPlayer(getActivity());
        }
    }

    private void savePlayer(Activity activity) {

        mPlayer = new Player(mFirstName.getText().toString(), mAge.getText().toString(),mHeight_feet.getText().toString(),mHeight_inches.getText().toString(),mWeight.getText().toString(),isfemale,
                mSelectedAvatar,activity_position);
        Log.i("MyMplayer",""+mPlayer.getAvatar().getDrawableId());
        PreferencesHelper.writeToPreferences(activity, mPlayer);
    }

    private boolean isAvatarSelected() {
        return mSelectedAvatarView != null || mSelectedAvatar != null;
    }

    public void isInputEmpty(){
        player_firstname = mFirstName.getText().toString();
        player_age = mAge.getText().toString();
        player_height = mHeight_feet.getText().toString();
        player_weight = mWeight.getText().toString();
    }
   /* private boolean isInputDataValid() {
        return PreferencesHelper.isInputDataValid(mFirstName.getText().toString(), mAge.getText().toString(), mHeight.getText().toString(),mWeight.getText().toString());
    }*/


    /**
     * Calculates spans for avatars dynamically.
     *
     * @return The recommended amount of columns.
     */
    private boolean isInputDataValid() {
        return PreferencesHelper.isInputDataValid(mFirstName.getText(), mAge.getText(),mHeight_feet.getText().toString(), mWeight.getText().toString());
    }
    private int calculateSpanCount() {
        int avatarSize = getResources().getDimensionPixelSize(R.dimen.size_fab);
        int avatarPadding = getResources().getDimensionPixelSize(R.dimen.spacing_double);
        return mAvatarGrid.getWidth() / (avatarSize + avatarPadding);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
