package com.example.memoryofagoldfishricardsmasniks.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.memoryofagoldfishricardsmasniks.R;
import com.example.memoryofagoldfishricardsmasniks.models.Score;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

public class ScoresFragment extends Fragment
{
    RecyclerView mRecyclerView;
    String mUrl;
    public static ScoresFragment newInstance()
    {
        ScoresFragment fragment = new ScoresFragment();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View v = inflater.inflate(R.layout.fragment_scores, container, false);

        Bundle extras = getActivity().getIntent().getExtras();
        if (extras != null )
        {
            mUrl = extras.getString("URL");
        }
        mRecyclerView = v.findViewById(R.id.recyclerview);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        String index = Uri.parse(mUrl).getLastPathSegment();
        ArrayList<Score> test = loadScoreList(getContext(), index);

        ScoreAdapter adapter = new ScoreAdapter(test);
        mRecyclerView.setAdapter(adapter);

        return v;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }


    public class ScoreHolder extends RecyclerView.ViewHolder
    {
        TextView mScoreTextView;
        TextView mTurnsTextView;
        TextView mSequenceTextView;
        TextView mTimeTextView;

        public ScoreHolder(@NonNull View v)
        {
            super(v);
            mScoreTextView = v.findViewById(R.id.scores_text);
            mTurnsTextView = v.findViewById(R.id.turns);
            mSequenceTextView = v.findViewById(R.id.max_sequence);
            mTimeTextView = v.findViewById(R.id.time_elapsed);
        }

    }


    public class ScoreAdapter extends RecyclerView.Adapter<ScoreHolder>
    {
        ArrayList<Score> mScoreList;

        public ScoreAdapter(ArrayList<Score> pScoreList)
        {
            mScoreList = pScoreList;
        }

        @NonNull
        @Override
        public ScoreHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
        {
            LayoutInflater inflater = LayoutInflater.from(getActivity());
            View view = inflater.inflate(R.layout.scoresrecyclerview_item, parent, false);

            return new ScoreHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ScoreHolder holder, int position)
        {
            if(mScoreList != null)
            {
                Score score = mScoreList.get(position);
                Integer turns = score.getTurns();
                Integer sequence = score.getSequence();
                Integer time = score.getTime();

                 holder.mTurnsTextView.setText(turns.toString());
                holder.mSequenceTextView.setText(sequence.toString());
                holder.mTimeTextView.setText(time.toString());
            }
        }

        @Override
        public int getItemCount()
        {
            return mScoreList.size();
        }
    }

    public ArrayList<Score> loadScoreList(Context context, String fileName)
    {
        ArrayList<Score> scoreList = new ArrayList<>();

        SharedPreferences sharedPref = context.getSharedPreferences(fileName + "Scores",
                Context.MODE_PRIVATE);
        String string =(sharedPref.getString("score", "no scores"));
        //If the score file hasnt been initialized create an empty score
        if(string == "no scores")
        {
            scoreList.add(new Score());
        }
        //Load the scores
        else
        {
            JSONArray jsonArray;
            try {
                jsonArray = new JSONArray(sharedPref.getString("score", "[]"));

                for(int i = jsonArray.length() - 1; i > 0; i = i -3)
                {
                    int turns = (int) jsonArray.get(i);
                    int sequence = (int) jsonArray.get(i - 1);
                    int time = (int) jsonArray.get(i -2);
                    Score score = new Score(time, sequence, turns);
                    scoreList.add(score);

                }

            } catch (JSONException e) {
                e.printStackTrace();
            }




        }


        return scoreList;
    }
}
