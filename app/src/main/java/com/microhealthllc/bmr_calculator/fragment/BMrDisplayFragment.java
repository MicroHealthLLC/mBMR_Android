package com.microhealthllc.bmr_calculator.fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.animation.FastOutLinearInInterpolator;
import android.support.v4.view.animation.FastOutSlowInInterpolator;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.microhealthllc.bmr_calculator.DB.BmiLogs;
import com.microhealthllc.bmr_calculator.DB.DBHandler;
import com.microhealthllc.bmr_calculator.LogActivity;
import com.microhealthllc.bmr_calculator.LogListAdapter;
import com.microhealthllc.bmr_calculator.LogModel;
import com.microhealthllc.bmr_calculator.R;
import com.microhealthllc.bmr_calculator.Spinner.NiceSpinner;
import com.microhealthllc.bmr_calculator.chart.ColorArcProgressBar;
import com.microhealthllc.bmr_calculator.chart.TickerUtils;
import com.microhealthllc.bmr_calculator.chart.TickerView;
import com.microhealthllc.bmr_calculator.helper.PreferencesHelper;
import com.microhealthllc.bmr_calculator.model.Player;
import com.microhealthllc.bmr_calculator.widget.AvatarView;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

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
    TextView binfoview;

    TextView current_date;
    private ColorArcProgressBar bar1;
    public static final DecimalFormat df1 = new DecimalFormat( "#.#" );
    private OnFragmentInteractionListener mListener;
    private RecyclerView recyclerView;
    private LogListAdapter mAdapter;
    private List<LogModel> logList = new ArrayList<>();
    public final int SEDENTARY = 0;
    public final int  LIGHTLY_ACTIVE = 0;
    public final int MODERATE =1;
    public final int VERY_ACTIVE =2;
    public final int  EXTRA_ACTIVE =4;
    private TickerView calories_needed_text;
    private static final char[] NUMBER_LIST = TickerUtils.getDefaultNumberList();
    DBHandler db;

    Button view_allbutton;

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

        }





    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (getArguments() != null) {
          //  player = getArguments().getParcelable(EXTRA_PLAYER);
           // Log.i("PlayerID","player id :"+player.getFirstName()+", drawable id="+player.getAvatar().getDrawableId()+"");
          //  Toast.makeText(getActivity(),"player.getFirstName()="+player.getFirstName()+","+player.getAvatar().getDrawableId(),Toast.LENGTH_SHORT).show();
        }

        db  = new DBHandler(getActivity());
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
        display_bmr = (TextView) getActivity().findViewById(R.id.display_bmr);

        binfoview = (TextView)getActivity().findViewById(R.id.binfo);

        setAvatarDrawable(avatar);
        current_date = (TextView)getActivity().findViewById(R.id.date_curr);
        current_date.setText(getDateTime());
        display_name.setText(PreferencesHelper.getPlayer(getActivity()).getFirstName());
        display_weight.setText(PreferencesHelper.getPlayer((getActivity())).getWeight());
        display_height.setText(Double.toString(calculateheightINCHES()));
        display_bmr.setText(df1.format(calculateBMR()));
        recyclerView = (RecyclerView) getActivity().findViewById(R.id.recycler_view);
        view_allbutton = (Button)getActivity().findViewById(R.id.view_more);
        mAdapter = new LogListAdapter(logList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        setBinfoView();
        recyclerView.setAdapter(mAdapter);
        view_allbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               startActivity( new Intent(getActivity(), LogActivity.class));
            }
        });
        calories_needed_text =(TickerView) getActivity().findViewById(R.id.dailycal);
        calories_needed_text. setCharacterList(TickerUtils.getDefaultNumberList());
        calories_needed_text.setText(df1.format(DailyCaloriesNeeded()));
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
        prepareMovieData();
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

    public  Double DailyCaloriesNeeded(){
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

    private List<BmiLogs> reverseList(List<BmiLogs> myList) {
        List<BmiLogs> invertedList = new ArrayList<BmiLogs>();
        for (int i = myList.size() - 1; i >= 0; i--) {
            invertedList.add(myList.get(i));
        }
        return invertedList;
    }


    private void prepareMovieData() {
        LogModel loghistory ;


        try {
            // Reading all shops
            Log.d("Reading:", "Reading all Logs.");
            List<BmiLogs> logs = db.getAllShops();
            List<BmiLogs> reverse;
            reverse =  reverseList(logs);


            // for (BmiLogs log : logs) {
            //     loghistory = new LogModel(log.getBmi(), log.getWeight(), log.getDateTime());
//  List<BmiLogs> logs = db.getAllShops();
            for (int i=logs.size()-1;i>logs.size()-3; --i){
                loghistory = new LogModel(logs.get(i).getBmi(), logs.get(i).getWeight(), logs.get(i).getCalories_needed(),logs.get(i).getDateTime());
                logList.add(loghistory);
            }




// Writing shops to log
            //      Log.d("BMILO: : ", "weight:" + log.getWeight() + "  BMI:" + log.getBmi() + "  DateTime:" + log.getDateTime());

            //  }

        } catch (Exception e) {
            Log.i("Thisexcept", e.toString());
        }


        mAdapter.notifyDataSetChanged();
    }
    public String getDateTime() {
        Calendar calendar = Calendar.getInstance();
        Date date = calendar.getTime();
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "EE MM/dd", Locale.getDefault());
        //Date date = new Date();
        return dateFormat.format(date);
    }


    public void setBinfoView() {
        double activity_levell = 0;
        PreferencesHelper.getPlayer(getActivity()).getActivitiy_level_position();
        if (PreferencesHelper.getPlayer(getActivity()).getActivitiy_level_position() == SEDENTARY) {
            activity_levell = 1.2;
        } else if (PreferencesHelper.getPlayer(getActivity()).getActivitiy_level_position() == LIGHTLY_ACTIVE){
            activity_levell = 1.375;
    }
    else if(PreferencesHelper.getPlayer(getActivity()).getActivitiy_level_position() == MODERATE) {
            activity_levell = 1.55;

        }

        else if (PreferencesHelper.getPlayer(getActivity()).getActivitiy_level_position() == VERY_ACTIVE) {
            activity_levell = 1.725;
        }
        else{
            activity_levell = 1.9;
        }
        binfoview.setText(Html.fromHtml(getString(R.string.bmr_info)) +""+"your activity factor("+activity_levell +") x BMR ("+df1.format(calculateBMR()) +") is "+df1.format(DailyCaloriesNeeded()));
    }


    public void setCalories() {
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {



                        handler.postDelayed(this, 1500);
                    }
                });
            }
        }, 2000);
    }



}

