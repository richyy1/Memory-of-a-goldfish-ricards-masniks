package com.example.memoryofagoldfishricardsmasniks.models;


/* This class contain data and methods relevant to the puzzle - list of tiles, bitmap of the default
tile etc */

import java.util.ArrayList;

public class Puzzle
{
    private String mName;
    private ArrayList<Tile> mTiles;
    private Tile mTileback;


    //Constructor
    public Puzzle()
    {
        //Initialize a default puzzle object
        ArrayList<Tile> test = new ArrayList<>();

        for(int i = 0; i <10; i++)
        {
            Tile tile = new Tile("Tile " + i);
            test.add(tile);
        }



        mName = "No puzzle";
        mTileback = null;
        mTiles = test;

    }

    //Getters

    //Setters
    public void setPuzzle(String pName, ArrayList<Tile> pTiles, Tile pTileBack)
    {
        mName = pName;
        mTiles = pTiles;
       mTileback = pTileBack;
    }



    public String getName() {
        return mName;
    }

    public ArrayList<Tile> getAllTiles() {
        return mTiles;
    }

    public Tile getTile(int index)
    {
        return mTiles.get(index);
    }



    public Tile getTileback() {
        return mTileback;
    }



    public void setTileback(Tile Tileback) {
        mTileback = Tileback;
    }
}
