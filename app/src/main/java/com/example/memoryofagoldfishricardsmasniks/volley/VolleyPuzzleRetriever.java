package com.example.memoryofagoldfishricardsmasniks.volley;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.example.memoryofagoldfishricardsmasniks.models.Puzzle;
import com.example.memoryofagoldfishricardsmasniks.models.Tile;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class VolleyPuzzleRetriever implements VolleyJSONObjectResponse, VolleyImageResponse {
    String mUrl;
    Context mContext;
    RequestQueue mRequestQueue;
    Puzzle mPuzzle;
    MutableLiveData<Puzzle> mPuzzleData;

    public VolleyPuzzleRetriever(String pUrl, Context pContext) {
        mUrl = pUrl;
        mContext = pContext;
        mRequestQueue = Volley.newRequestQueue(pContext);
        mPuzzle = new Puzzle();
        mPuzzleData = new MutableLiveData<>();
    }


    //Download the Puzzle data using Volley, and parse it into a Puzzle object.
    public LiveData<Puzzle> getPuzzle()
    {

        //Only download the puzzle if it cant be loaded locally
        if(!loadPuzzleLocally())
        {
            //Load locally if possible
            loadPuzzleLocally();
            //Otherwise download it
            AcceptSSLCerts.accept();
            CustomJSONObjectRequest request = new CustomJSONObjectRequest(Request.Method.GET,
                    mUrl,
                    null,
                    "PuzzleJSON",
                    this);

            mRequestQueue.getCache().clear();
            mRequestQueue.add(request.getJsonObjectRequest());
        }

        return mPuzzleData;

    }

    private Puzzle parseJsonResponse(JSONObject response)
    {
        Puzzle puzzle = new Puzzle();
        try {
            String oName = response.getString("name");
            String tileBack = response.getString("TileBack");

            String tileBackUrl ="https://www.goparker.com/600096/moag/images/" + tileBack + ".png";



            ArrayList<Tile> tiles = parseJsonTiles(response);

            Tile tilebackTile = new Tile(tileBack);
            com.memory.goldfish.volley.CustomImageRequest imageRequest = new com.memory.goldfish.volley.CustomImageRequest(tilebackTile.getImageUrl(), tilebackTile, this);

            savePuzzleLocally(response);
            //Save puzzle locally
            mRequestQueue.add(imageRequest.getImageRequest());



            puzzle.setPuzzle(oName, tiles, tilebackTile);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return puzzle;
    }


    private ArrayList<Tile> parseJsonTiles(JSONObject response)
    {
        ArrayList<Tile> pictureSet = new ArrayList<>();

        try {
            //PictureSet it will find --> PictureSet object from the url and all
            //images names
            JSONArray pictureSetArray = response.getJSONArray("PictureSet");

            for (int i = 0; i < pictureSetArray.length(); i++) {
                String pictureName = pictureSetArray.getString(i);
                // Bitmap pictureBitmap;
                //pictureBitmap = getBitmap(pictureName);
                Tile tile = new Tile(pictureName);


               com.memory.goldfish.volley.CustomImageRequest imageRequest = new com.memory.goldfish.volley.CustomImageRequest(tile.getImageUrl(), tile, this);

               mRequestQueue.add(imageRequest.getImageRequest());


                pictureSet.add(tile);
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }

        return pictureSet;
    }


    //Methods related to internal storage, this method download puzzles from the url
    public void savePuzzleLocally(JSONObject response) throws JSONException
    {

        String theme = Uri.parse(mUrl).getLastPathSegment();
        String name = response.getString("name");
        String tilebackName = response.getString("TileBack");

        SharedPreferences sharedPref = mContext.getSharedPreferences(theme, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor;
        if(!sharedPref.contains("downloaded")) {
            editor = sharedPref.edit();

            editor.putString("name", name);
            editor.putString("tileback", tilebackName);

            JSONArray jsonArray = response.getJSONArray("PictureSet");
            editor.putString("tiles", jsonArray.toString());
            editor.putBoolean("downloaded", true);

            editor.apply();

        }



    }
    public boolean loadPuzzleLocally()
    {

        String puzzleName = Uri.parse(mUrl).getLastPathSegment();
        SharedPreferences sharedPref = mContext.getSharedPreferences(puzzleName, Context.MODE_PRIVATE);

        //If the file has been downloaded
        if(sharedPref.contains("downloaded"))
        {
            String name = sharedPref.getString("name", "no name");
            String tileback = sharedPref.getString("tileback", "no tileback");
            JSONArray jsonArray = null;
            try {
                jsonArray = new JSONArray(sharedPref.getString("tiles", "[]"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            MutableLiveData<Tile> tilebackLiveData = new MutableLiveData<>();
            Tile tilebackTile = new Tile(tileback);
            tilebackLiveData.setValue(tilebackTile);
            loadImageLocally(tileback, tilebackLiveData);
            ArrayList<Tile> tiles = new ArrayList<>();
            for(int i = 0; i < jsonArray.length(); i++ )
            {
                try {
                    String tileName = jsonArray.getString(i);
                    MutableLiveData<Tile> tileMutableLiveData = new MutableLiveData<>();

                    Tile tile = new Tile(tileName);
                    tileMutableLiveData.setValue(tile);
                    Log.d("Internal Storage", tileName + " name loaded");
                    loadImageLocally(tileName, tileMutableLiveData);
                    tiles.add(tileMutableLiveData.getValue());

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
           mPuzzle.setPuzzle(name, tiles, tilebackLiveData.getValue());
            mPuzzleData.setValue(mPuzzle);
           // mPuzzleData
            return  true;
        }

        return false;
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
                Log.d("Internal Storage", pFileName + " saved to internal storage");
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

    public boolean loadImageLocally(String pFileName, MutableLiveData<Tile> pTile)
    {
        boolean loaded = false;

        ContextWrapper contextWrapper = new ContextWrapper(mContext);
        File directory = contextWrapper.getDir("tileImages", Context.MODE_PRIVATE);
        File file = new File(directory, pFileName);

        if(file.exists())
        {
            FileInputStream fileInputStream = null;
            try
            {
                fileInputStream = new FileInputStream(file);
                Bitmap bitmap = BitmapFactory.decodeStream(fileInputStream);
                Tile tile = pTile.getValue();
                tile.setBitmap(bitmap);

                pTile.postValue(tile);
                loaded = true;
                Log.d("Internal Storage", tile.getName() + " sucessfully loaded");
            }
            catch (FileNotFoundException e)
            {
                e.printStackTrace();
            }
        }

        return loaded;
    }



    //JSONObjectResponse overridden methods
    @Override
    public void onResponse(JSONObject pObject, String pTag)
    {
        mPuzzle = parseJsonResponse(pObject);
        mPuzzleData.setValue(mPuzzle);
        Log.e(pTag, "Puzzle download finished");
    }

    @Override
    public void onError(VolleyError pError, String pTag)
    {
        Log.e(pTag, "Puzzle download failed: " + pTag);

    }

    @Override
    public void onResponse(Bitmap pBitmap, Tile pTile)
    {
        Log.e("ImageRequest", "Image retrieved for: " + pTile.getName());
        saveImageLocally(pBitmap, pTile.getName());
        pTile.setBitmap(pBitmap);
        mPuzzleData.setValue(mPuzzle);

    }




}