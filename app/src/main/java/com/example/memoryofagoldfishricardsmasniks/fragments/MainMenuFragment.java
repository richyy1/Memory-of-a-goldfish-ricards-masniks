package com.example.memoryofagoldfishricardsmasniks.fragments;


import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.memoryofagoldfishricardsmasniks.R;
import com.example.memoryofagoldfishricardsmasniks.activities.PlayActivity;
import com.example.memoryofagoldfishricardsmasniks.activities.ScoresActivity;

public class MainMenuFragment extends Fragment
{
    Button mSettingsButton;
    Button mPlayButton;
    Button mAchievementsButton;
    Button mHighScoresButton;
    String mUrl;
    String TAG_URL = "URL";

    public MainMenuFragment()
    {

    }

    public static MainMenuFragment newInstance()
    {
        MainMenuFragment fragment = new MainMenuFragment();

        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View v = inflater.inflate(R.layout.fragment_main_menu, container, false);

        mHighScoresButton = v.findViewById(R.id.highScores_button);
        mPlayButton = v.findViewById(R.id.play_button);


        mPlayButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(getContext(), PlayActivity.class);

                Bundle extras = getActivity().getIntent().getExtras();
                if (extras != null && extras.getString("URL") != null)
                {
                    mUrl = extras.getString("URL");
                    intent.putExtra(TAG_URL, mUrl);
                }


                getActivity().finish();
                startActivity(intent);
            }
        });

        mHighScoresButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

                Intent intent = new Intent(getContext(), ScoresActivity.class);

                Bundle extras = getActivity().getIntent().getExtras();
                if (extras != null && extras.getString("URL") != null)
                {
                    mUrl = extras.getString("URL");
                    intent.putExtra(TAG_URL, mUrl);
                }
                startActivity(intent);


            }
        });

        return v;
    }
}
