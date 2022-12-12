package com.example.memoryofagoldfishricardsmasniks.models;


import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Bitmap;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;

import com.example.memoryofagoldfishricardsmasniks.volley.VolleyPuzzleRetriever;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

//This class contains ALL the data relevant to the game
public class Game
{
    private static Game sGame;
    private final Context mContext;
    private VolleyPuzzleRetriever mPuzzleRetriever;
    String mUrl;
    private final MediatorLiveData<Puzzle> mPuzzle;

    ArrayList<Score> mHighScores;

    private Game(Context pApplicationContext)
    {
        this.mContext = pApplicationContext;
        mPuzzle = new MediatorLiveData<>();
        mHighScores = new ArrayList<>();
    }

    public static Game getInstance(Context pApplicationContext)
    {
        if(sGame == null)
        {
            sGame = new Game(pApplicationContext);
        }
        return sGame;
    }

    public void saveImageLocally(Bitmap pBitmap, String pFileName)
    {
        ContextWrapper contextWrapper = new ContextWrapper(mContext);
        File directory = contextWrapper.getDir("tileImages", Context.MODE_PRIVATE);
        File file = new File(directory, pFileName);

        if(!file.exists())
        {
            FileOutputStream fileOutputStream = null;

            try
            {
             fileOutputStream = new FileOutputStream(file);
             pBitmap.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream);
             fileOutputStream.flush();
             fileOutputStream.close();
            }
            catch (FileNotFoundException e)
            {
                e.printStackTrace();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }


    public LiveData<Puzzle> getPuzzle(String pUrl)
    {
        mUrl = pUrl;
        mPuzzleRetriever = new VolleyPuzzleRetriever(pUrl, mContext);
        LiveData<Puzzle> remoteData = mPuzzleRetriever.getPuzzle();

        mPuzzle.addSource(remoteData, value-> mPuzzle.setValue(value));

        return mPuzzle;
    }


    public void setScore(Score score)
    {
        mHighScores.add(score);
    }

}
