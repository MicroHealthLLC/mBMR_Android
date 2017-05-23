package com.microhealthllc.bmr_calculator.fragment;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.animation.FastOutLinearInInterpolator;
import android.support.v4.view.animation.FastOutSlowInInterpolator;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.microhealthllc.bmr_calculator.R;
import com.microhealthllc.bmr_calculator.Spinner.NiceSpinner;
import com.microhealthllc.bmr_calculator.chart.ColorArcProgressBar;
import com.microhealthllc.bmr_calculator.helper.PreferencesHelper;
import com.microhealthllc.bmr_calculator.model.Player;
import com.microhealthllc.bmr_calculator.widget.AvatarView;

import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link BMrDisplayFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link BMrDisplayFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BMrDisplayFragment extends Fragment  {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String EXTRA_PLAYER = "player";
    AvatarView avatar;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    Player player;
    private TextView display_name;
    private TextView display_height;
    private TextView display_weight;
    private TextView display_bmr;
    private ColorArcProgressBar bar1;
    public static final DecimalFormat df1 = new DecimalFormat( "#.#" );
    private OnFragmentInteractionListener mListener;


    public final int SEDENTARY = 0;
    public final int  LIGHTLY_ACTIVE = 0;
    public final int MODERATE =1;
    public final int VERY_ACTIVE =2;
    public final int  EXTRA_ACTIVE =4;

    public BMrDisplayFragment() {
        // Required empty public constructor
    }
    public static BMrDisplayFragment newInstance() {
        return new BMrDisplayFragment();
    }
    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param player Parameter 1.

     * @return A new instance of fragment BMrDisplayFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static BMrDisplayFragment newInstance(Player player) {
        BMrDisplayFragment fragment = new BMrDisplayFragment();
        Bundle args = new Bundle();
        args.putParcelable(EXTRA_PLAYER, player);
        //args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



        //    mParam2 = getArguments().getString(ARG_PARAM2);


        }





    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (getArguments() != null) {
          //  player = getArguments().getParcelable(EXTRA_PLAYER);
           // Log.i("PlayerID","player id :"+player.getFirstName()+", drawable id="+player.getAvatar().getDrawableId()+"");
          //  Toast.makeText(getActivity(),"player.getFirstName()="+player.getFirstName()+","+player.getAvatar().getDrawableId(),Toast.LENGTH_SHORT).show();
        }

        bar1 = (ColorArcProgressBar) getActivity().findViewById(R.id.bar1);
    //    bar1.setcolors(Color.YELLOW,Color.YELLOW,Color.YELLOW);
        bar1.setcolor3(Color.GREEN);
        bar1.setcolor1(Color.GREEN);
        bar1.setcolor2(Color.GREEN);
        bar1.setCurrentValues(calculateBMR());
        avatar  = (AvatarView) getActivity().findViewById(R.id.avatar);
        display_name = (TextView) getActivity().findViewById(R.id.displayname);
        display_height = (TextView) getActivity().findViewById(R.id.display_height);
        display_weight = (TextView)getActivity().findViewById(R.id.display_weight);
        display_bmr = (TextView)getActivity().findViewById(R.id.display_bmr);
        setAvatarDrawable(avatar);
        display_name.setText(PreferencesHelper.getPlayer(getActivity()).getFirstName());
        display_weight.setText(PreferencesHelper.getPlayer((getActivity())).getWeight());
        display_height.setText(Double.toString(calculateheightINCHES()));
        display_bmr.setText(df1.format(calculateBMR()));



        final CardView cardView = (CardView) getActivity().findViewById(R.id.inset_card);
        final View fab = getActivity().findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cardView.getTranslationY() > 0) {
                    cardView
                            .animate()
                            .translationY(0)
                            .setInterpolator(new FastOutSlowInInterpolator())
                            .start();
                } else {
                    cardView
                            .animate()
                            .translationY(cardView.getHeight())
                            .setInterpolator(new FastOutSlowInInterpolator())
                            .start();
                }

            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        return inflater.inflate(R.layout.fragment_bmr_display, container, false);

    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }



    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    private void setAvatarDrawable(AvatarView avatarView) {
        Player player = PreferencesHelper.getPlayer(getActivity());
        avatarView.setAvatar(player.getAvatar().getDrawableId());
        ViewCompat.animate(avatarView)
                .setInterpolator(new FastOutLinearInInterpolator())
                .setStartDelay(500)
                .scaleX(1)
                .scaleY(1)
                .start();
    }

    public Double calculateheightINCHES() {
        double inches = 0;
        double feets = Double.parseDouble(PreferencesHelper.getPlayer(getActivity()).getHeight_feets());
        if (PreferencesHelper.getPlayer(getActivity()).getHeight_inch() != null) {
            inches = Double.parseDouble(PreferencesHelper.getPlayer(getActivity()).getHeight_inch());
        }
        ;
        return ((feets * 12) + (inches));
    }

public Double calculateBMR(){
    //women
    /*
    Women: BMR = 655 + (4.35 x weight in pounds) + (4.7 x height in inches) - (4.7 x age in years)

   Men: BMR = 66 + (6.23 x weight in pounds) + (12.7 x height in inches) - (6.8 x age in years)
    * */
    Double BMR;
    Double weight = Double.parseDouble(PreferencesHelper.getPlayer(getActivity()).getWeight());
    Double height = calculateheightINCHES();
    Double age = Double.parseDouble(PreferencesHelper.getPlayer(getActivity()).getAge());
    if (PreferencesHelper.getPlayer(getActivity()).getIsfemale()){
      BMR =  655 +(4.35 * weight)+(4.7 * height)-(4.7*age);
    }
    else {
        BMR = 66 +(6.23 * weight)+(12.7 * height)-(6.8 *age);
    }
    //man
        return BMR;
    }

    public Double DailyCaloriesNeeded(){
        Double DailyCaloriies = 0.0;
        Double BMR = calculateBMR();
        int activity_level = PreferencesHelper.getPlayer(getActivity()).getActivitiy_level_position();
         if(activity_level == SEDENTARY){
             DailyCaloriies =  BMR *1.2;

         }

        else if(activity_level == LIGHTLY_ACTIVE){
             DailyCaloriies = BMR * 1.375;
         }
         else if (activity_level == MODERATE){
             DailyCaloriies = BMR * 1.55;
         }

         else if (activity_level == VERY_ACTIVE){
             DailyCaloriies = BMR * 1.725;
         }
         else {
             DailyCaloriies = BMR * 1.9;
         }
        return DailyCaloriies;
    }

}

