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
import android.widget.Toast;

import com.microhealthllc.bmr_calculator.R;
import com.microhealthllc.bmr_calculator.Spinner.NiceSpinner;
import com.microhealthllc.bmr_calculator.chart.ColorArcProgressBar;
import com.microhealthllc.bmr_calculator.helper.PreferencesHelper;
import com.microhealthllc.bmr_calculator.model.Player;
import com.microhealthllc.bmr_calculator.widget.AvatarView;

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
    private ColorArcProgressBar bar1;
    private OnFragmentInteractionListener mListener;

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
            player = getArguments().getParcelable(EXTRA_PLAYER);
            Log.i("PlayerID","player id :"+player.getFirstName()+", drawable id="+player.getAvatar().getDrawableId()+"");
            Toast.makeText(getActivity(),"player.getFirstName()="+player.getFirstName()+","+player.getAvatar().getDrawableId(),Toast.LENGTH_SHORT).show();
        }

        bar1 = (ColorArcProgressBar) getActivity().findViewById(R.id.bar1);
    //    bar1.setcolors(Color.YELLOW,Color.YELLOW,Color.YELLOW);
        bar1.setcolor3(Color.GREEN);
        bar1.setcolor1(Color.GREEN);
        bar1.setcolor2(Color.GREEN);
        bar1.setCurrentValues(1500);
        avatar  = (AvatarView) getActivity().findViewById(R.id.avatar);
        setAvatarDrawable(avatar);

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
}
