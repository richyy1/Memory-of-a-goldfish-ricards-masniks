package com.example.memoryofagoldfishricardsmasniks.fragments;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.SystemClock;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;

import com.example.memoryofagoldfishricardsmasniks.activities.MainMenuActivity;
import com.example.memoryofagoldfishricardsmasniks.models.Achievements;
import com.example.memoryofagoldfishricardsmasniks.viewmodel.GameViewModel;
import com.example.memoryofagoldfishricardsmasniks.R;
import com.example.memoryofagoldfishricardsmasniks.models.Puzzle;
import com.example.memoryofagoldfishricardsmasniks.models.Score;
import com.example.memoryofagoldfishricardsmasniks.models.Tile;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PlayFragment extends Fragment {
    private RecyclerView recyclerView;
    private GameViewModel mGameViewModel;

    public PlayFragment() {
        // Required empty public constructor
    }

    public static PlayFragment newInstance() {
        PlayFragment fragment = new PlayFragment();
        return fragment;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_play, container, false);

        //Get URL from intent
        Bundle extras = getActivity().getIntent().getExtras();
        if (extras != null) {
            String url = extras.getString("URL");
            mGameViewModel = new GameViewModel(getActivity().getApplication(), url);

        }
        else
        {
            mGameViewModel = new GameViewModel(getActivity().getApplication(), "");

        }


        recyclerView = v.findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new GridLayoutManager(requireContext(), 5));
        recyclerView.setNestedScrollingEnabled(false);

        final Observer<Puzzle> puzzleObserver = puzzle ->
        {
           PlayAdapter newAdapter = new PlayAdapter(puzzle);
           recyclerView.setAdapter(newAdapter);

        };
        mGameViewModel.getPuzzle().observe(getViewLifecycleOwner(), puzzleObserver);

        return v;
    }



    //RecyclerView
    public class PlayHolder extends RecyclerView.ViewHolder {
        private final ImageView mItemImageView;
        Boolean mSolved;
        Boolean mClicked;

        public PlayHolder(View itemView) {
            super(itemView);
            mItemImageView = itemView.findViewById(R.id.playRecyclerViewImageView);
            mSolved = false;
            mClicked = false;
        }

        public void bindBitmap(Bitmap bitmap) {
            if (bitmap == null) {
                mItemImageView.setImageDrawable(getResources().getDrawable(R.drawable.placeholder));
            } else {
                Drawable drawable = new BitmapDrawable(getResources(), bitmap);
                mItemImageView.setImageDrawable(drawable);
            }
        }
    }

    public class PlayAdapter extends RecyclerView.Adapter<PlayHolder> {


        Puzzle mPuzzle;
        ArrayList<Tile> mTiles;
        Tile mTileback;
        Score mScore;

        //Member variables related to the game logic
        List<Pair<Integer, String>> mSelectedTilesPositions;
        ArrayList<Integer> mSolvedTiles;

        //Member variables related to score
        int mTurns;
        int mHighestSequence;
        int mSequence;
        long mStartTime;

        //Achievements
        Achievements mAchievements;

        public PlayAdapter(Puzzle puzzle) {
            mPuzzle = puzzle;
            mScore = new Score();
            mAchievements = new Achievements();

            mSelectedTilesPositions = new ArrayList<>(Arrays.asList());
            mSolvedTiles = new ArrayList<>();
            mTileback = mPuzzle.getTileback();

            mTiles = puzzle.getAllTiles();
            if (mTiles.size() < 40) {
                mTiles.addAll(mTiles);
            }

            mTurns = 0;
            mSequence = 0;
            mStartTime = SystemClock.elapsedRealtime();
            // Collections.shuffle(mTiles);
        }

        @NonNull
        @Override
        public PlayHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(getActivity());
            View view = inflater.inflate(R.layout.playrecyclerview_item, parent, false);

            return new PlayHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull PlayHolder holder, @SuppressLint("RecyclerView") int position) {
            //If the position is in solved tiles
            //Spin!@
            if (mSolvedTiles.contains(position)) {
                rotateTile(holder);
                holder.mSolved = true;
                holder.bindBitmap(mTiles.get(position).getBitmap());
                return;
            }

            //If the position is Currently selected
            if (mSelectedTilesPositions.contains(Pair.create(position, mTiles.get(position).getName()))) {
                holder.bindBitmap(mTiles.get(position).getBitmap());
                holder.mClicked = true;
            }

            //If the puzzle hasnt been downloaded yet
            //use a placeholder image
            if (mPuzzle.getName() == "No puzzle") {
                holder.bindBitmap(null);
            }
            //Else use the downloaded image
            else if (mTileback.getBitmap() != null) {
                holder.bindBitmap(mTileback.getBitmap());
                holder.mItemImageView.setBackgroundResource(R.color.black);
                //  holder.mItemImageView.setForeground();
            }

            holder.mItemImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //If the tile is solved
                    if (mSolvedTiles.contains(position)) {
                        //Do nothing
                        return;
                    } else if (holder.mClicked) {
                        //Increment the turns variable
                        mTurns++;
                        //Unselect the tile
                        holder.mClicked = false;
                        removeSelectedTile(position);
                        //Do the flip animation
                        holder.mItemImageView.setRotationY(0f);
                        holder.mItemImageView.animate().rotationY(90f).setListener(new AnimatorListenerAdapter() {
                            @Override
                            public void onAnimationEnd(Animator animation) {
                                holder.mItemImageView.setRotationY(270f);
                                holder.mItemImageView.animate().rotationY(360f).setListener(null);
                                //Change the bitmap
                                holder.bindBitmap(mTileback.getBitmap());
                            }
                        });

                    }
                    //If the tile is not selected
                    else {
                        //Increment the turns variable
                        mTurns++;

                        //If the list is full
                        if (isSelectedTilesFull()) {
                            //Do nothing
                            return;
                        }

                        //If the list isnt full
                        else {
                            //Change clicked to true
                            holder.mClicked = true;
                            //Add it to the list
                            addSelectedTile(position, mTiles.get(position).getName());

                            //Do the flip animation
                            holder.mItemImageView.setRotationY(0f);
                            holder.mItemImageView.animate().rotationY(90f).setListener(new AnimatorListenerAdapter() {
                                @Override
                                public void onAnimationEnd(Animator animation) {
                                    holder.mItemImageView.setRotationY(270f);
                                    holder.mItemImageView.animate().rotationY(360f).setListener(null);

                                    //Change the image
                                    holder.bindBitmap(mTiles.get(position).getBitmap());

                                    //Check if the list is full now
                                    if (isSelectedTilesFull()) {
                                        int secondTile = (mSelectedTilesPositions.get(0).first + mSelectedTilesPositions.get(1).first) - position;

                                        //If it is, check for pair
                                        if (checkForPair()) {
                                            //Rotate both tiles
                                            rotateTile(holder);
                                            notifyItemChanged(secondTile);
                                            mSelectedTilesPositions.clear();

                                            //Increment the sequence variable
                                            mSequence++;
                                            if (mSequence > mHighestSequence) {
                                                mHighestSequence = mSequence;
                                            }

                                            //Check if the game is won
                                            checkGameScore();
                                        } else {
                                            mSequence = 0;
                                        }
                                    }
                                }
                            });
                        }
                    }
                }
            });
        }

        @Override
        public int getItemCount() {
            return mTiles.size();
        }

        private void removeSelectedTile(int position) {
            for (int i = 0; i < mSelectedTilesPositions.size(); i++) {
                if (mSelectedTilesPositions.get(i).first == position) {
                    mSelectedTilesPositions.remove(i);
                }
            }
        }

        private void addSelectedTile(int position, String name) {
            mSelectedTilesPositions.add(new Pair<>(position, name));
        }

        private boolean isSelectedTilesFull() {
            return mSelectedTilesPositions.size() == 2;
        }

        private boolean checkForPair() {

            //Check if the currently selected tiles are equal
            if (mSelectedTilesPositions.get(0).second ==
                    mSelectedTilesPositions.get(1).second) {
                Log.d("Tile", "Pair found!");
                //Add the two positions to SolvedTiles array
                int firstPosition = mSelectedTilesPositions.get(0).first;
                int secondPosition = mSelectedTilesPositions.get(1).first;

                mSolvedTiles.add(firstPosition);
                mSolvedTiles.add(secondPosition);
                return true;

            }
            //
            else {
                Log.d("Tile", "Pair NOT found!");

                return false;
            }
        }

        public void rotateTile(PlayHolder holder) {
            RotateAnimation rotate = new RotateAnimation(0, 360, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
            rotate.setDuration(1000);
            rotate.setInterpolator(new LinearInterpolator());
            holder.mItemImageView.startAnimation(rotate);
            holder.mSolved = true;
        }


        private void checkGameScore() {
            checkAchievements();

            if (mSolvedTiles.size() == mTiles.size()) {


                //Stop the timer
                long endTime = SystemClock.elapsedRealtime();
                long delta = endTime - mStartTime;
                long elapsedTime = delta / 1000;

                //Update the score object
                //mTurns is how many times we turn the tiles
                updateScore(mTurns, mHighestSequence, (int) elapsedTime);

                //Build the dialog
                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());
                String index = Uri.parse(mGameViewModel.getMUrl()).getLastPathSegment();
                mScore.addNewScore(getContext(), index);


                dialogBuilder.setTitle(R.string.congratulations_string);
                dialogBuilder.setMessage(getString(R.string.num_turns) + mTurns +
                        "\n" + getString(R.string.match_sequence_string) + mHighestSequence + "\n" +
                        getResources().getString(R.string.timer_message) + elapsedTime
                );
                dialogBuilder.setPositiveButton(R.string.new_game, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        //Reset the game
                        getActivity().recreate();
                    }
                });

                dialogBuilder.setNegativeButton(R.string.main_menu, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        Intent intent = new Intent(getContext(), MainMenuActivity.class);
                        intent.putExtra("URL", mGameViewModel.getMUrl());
                        getActivity().finish();
                        startActivity(intent);

                    }
                });
                AlertDialog dialog = dialogBuilder.create();
                dialog.show();
                //Update the game singleton with scores

            }
        }

        private void updateScore(int turns, int sequence, int time) {

            mScore.setSequence(sequence);
            mScore.setTurns(turns);
            mScore.setTime(time);

            mGameViewModel.setScore(mScore);
        }


        private void checkAchievements() {
            if (mTurns == 2 && mSolvedTiles.size() == 2) {
                mAchievements.setFirst_Time(true);
            }

            if (mSequence >= 3 || mHighestSequence >= 3) {
                mAchievements.setThree_In_Row(true);
            }

            if (mSequence >= 5 || mHighestSequence >= 5) {
                mAchievements.setFive_In_Row(true);
            }

            if (mTiles.size() == mSolvedTiles.size() && mTurns < (mTiles.size() * 2)) {
                mAchievements.setPerf_Recall(true);
            }

            if (mTurns == mTiles.size() && mTiles.size() == mSolvedTiles.size()) {
                mAchievements.setFlawless(true);
            }
        }


    }

}