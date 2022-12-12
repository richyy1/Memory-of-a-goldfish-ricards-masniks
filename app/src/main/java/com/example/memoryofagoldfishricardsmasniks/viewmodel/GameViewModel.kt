package com.example.memoryofagoldfishricardsmasniks.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.memoryofagoldfishricardsmasniks.models.Game
import androidx.lifecycle.LiveData
import com.example.memoryofagoldfishricardsmasniks.models.Puzzle
import com.example.memoryofagoldfishricardsmasniks.models.Score
import com.example.memoryofagoldfishricardsmasniks.models.Tile

class GameViewModel(application: Application, pUrl: String?) : AndroidViewModel(application) {
    private val mGame: Game
    var mUrl: String? = null
    private var mPuzzle: LiveData<Puzzle>? = null
    val puzzle: LiveData<Puzzle>?
        get() {
            if (mPuzzle == null) {
                mPuzzle = mGame.getPuzzle(mUrl)
            }
            return mPuzzle
        }

    fun setScore(score: Score?) {
        mGame.setScore(score)
    }

    fun getTile(pName: String): LiveData<Tile>? {
        val temp = mPuzzle!!.value!!.allTiles

        //Look for the desired Tile
        for (tile in temp) {
            if (tile.name === pName) {
            }
        }
        return null
    }

    init {
        mGame = Game.getInstance(application.applicationContext)
        //If the url hasnt been provided then that means that a fragments model has been created already (maybe)?
        if (pUrl != null) {
            mUrl = pUrl
        }
        puzzle
    }
}