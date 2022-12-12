package com.example.memoryofagoldfishricardsmasniks.activities;

import androidx.fragment.app.Fragment;

import com.example.memoryofagoldfishricardsmasniks.fragments.ScoresFragment;

public class ScoresActivity extends SingleFragmentActivity
{

    @Override
    protected Fragment createFragment() {
        return ScoresFragment.newInstance();
    }
}
