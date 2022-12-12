package com.example.memoryofagoldfishricardsmasniks.models;


/* This class contain data and methods relevant to a single tile - name and bitmap, etc */

import android.graphics.Bitmap;

public class Tile
{
    private final String mName;
    private Bitmap mBitmap;
    private final String mImageUrl;
    private boolean mTileSolved;
    private boolean mTileClicked;

    public Tile(String pName)
    {
        mName = pName;
        mImageUrl ="https://www.goparker.com/600096/moag/images/" + mName + ".png";
        mTileSolved = false;
        mTileClicked = false;
    }

    public String getName() {
        return mName;
    }

    public String getImageUrl()
    {

        return mImageUrl;
    }

    public Bitmap getBitmap()
    {
        return mBitmap;
    }

    public void setBitmap(Bitmap mBitmap) {
        this.mBitmap = mBitmap;
    }

    public boolean isTileSolved() {
        return mTileSolved;
    }

    public void setTileSolved(boolean mTileSolved) {
        this.mTileSolved = mTileSolved;
    }

    public boolean isTileClicked() {
        return mTileClicked;
    }

    public void setTileClicked(boolean mTileClicked) {
        this.mTileClicked = mTileClicked;
    }
}
