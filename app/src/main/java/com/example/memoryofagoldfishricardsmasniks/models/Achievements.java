package com.example.memoryofagoldfishricardsmasniks.models;

public class Achievements
{
    private  boolean mFirst_Time;
    private  boolean mThree_In_Row;
    private  boolean mFive_In_Row;
    private  boolean mPerf_Recall;
    private  boolean mNon_Stop;
    private  boolean mFlawless;

    public Achievements()
    {
        mFirst_Time = false;
        mThree_In_Row = false;
        mFive_In_Row = false;
        mPerf_Recall = false;
        mNon_Stop = false;
        mFlawless = false;
    }

    public boolean isFirst_Time() {
        return mFirst_Time;
    }

    public void setFirst_Time(boolean mFirst_Time) {
        this.mFirst_Time = mFirst_Time;
    }

    public boolean isThree_In_Row() {
        return mThree_In_Row;
    }

    public void setThree_In_Row(boolean mThree_In_Row) {
        this.mThree_In_Row = mThree_In_Row;
    }

    public boolean isFive_In_Row() {
        return mFive_In_Row;
    }

    public void setFive_In_Row(boolean mFive_In_Row) {
        this.mFive_In_Row = mFive_In_Row;
    }

    public boolean isPerf_Recall() {
        return mPerf_Recall;
    }

    public void setPerf_Recall(boolean mPerf_Recall) {
        this.mPerf_Recall = mPerf_Recall;
    }

    public boolean isNon_Stop() {
        return mNon_Stop;
    }

    public void setNon_Stop(boolean mNon_Stop) {
        this.mNon_Stop = mNon_Stop;
    }

    public boolean isFlawless() {
        return mFlawless;
    }

    public void setFlawless(boolean mFlawless) {
        this.mFlawless = mFlawless;
    }
}
