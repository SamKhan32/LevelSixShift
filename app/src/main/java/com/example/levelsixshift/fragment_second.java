package com.example.levelsixshift;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link fragment_second#newInstance} factory method to
 * create an instance of this fragment.
 */
public class fragment_second extends Fragment {
    // declare Context
    Context context;
    // declare ButtonClick
    private ButtonClickListener buttonClickListener;
    // declare xml objects
    Button btnGame;
    Button btnLevel;
    Button addMoney ;
    Button setToHundred;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER


    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    // declare pseudo global variables
    private int level = -1;
    private int experience = -1;
    private int money = -1;
    private String avatar = "";
    private static final String moneyKey = "levelsixshift/money";
    private static final String levelKey = "levelsixshift/level";
    private static final String experienceKey = "levelsixshift/experience";


    public fragment_second() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment fragment_second.
     */
    // TODO: Rename and change types and number of parameters
    public static fragment_second newInstance(String param1, String param2) {

        fragment_second fragment = new fragment_second();
        Bundle args = new Bundle();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        if (getArguments() != null) {

        }



    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        // Ensure that the hosting activity implements the ButtonClickListener interface
        if (context instanceof ButtonClickListener) {
            buttonClickListener = (ButtonClickListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement ButtonClickListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        buttonClickListener = null;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_second, container, false);
        setUpElements(rootView);

        return rootView;
    }

    private void setUpElements(View rootView) {
         btnGame = rootView.findViewById(R.id.btnGame);
         addMoney = rootView.findViewById(R.id.button);
         setToHundred = rootView.findViewById(R.id.hundred);
         btnLevel = rootView.findViewById(R.id.btnLevel);

        //set up onClicks
        btnGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (buttonClickListener != null) {
                    buttonClickListener.onButtonClick(v);

                }
            }
        });
        addMoney.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        if (buttonClickListener != null) {
            buttonClickListener.onButtonClick(v);

        }
    }
});
        setToHundred.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (buttonClickListener != null) {
                    buttonClickListener.onButtonClick(v);

                }
            }
        });
        btnLevel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (buttonClickListener != null) {


                }
            }
        });

    }


    private void saveGlobalData() {
        ((RadioNoise) this.getActivity().getApplication()).setMoney(money);
        ((RadioNoise) this.getActivity().getApplication()).setAvatar(avatar);
        ((RadioNoise) this.getActivity().getApplication()).setLevel(level);
        ((RadioNoise) this.getActivity().getApplication()).setExperience(experience);
    }
    private void loadGlobalData() {
        ((RadioNoise) this.getActivity().getApplication()).getMoney();
        ((RadioNoise) this.getActivity().getApplication()).getAvatar();
        ((RadioNoise) this.getActivity().getApplication()).getLevel();
        ((RadioNoise) this.getActivity().getApplication()).getExperience();
    }
}
