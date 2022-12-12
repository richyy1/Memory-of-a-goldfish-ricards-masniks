package com.example.memoryofagoldfishricardsmasniks.activities;

import androidx.fragment.app.Fragment;

import com.example.memoryofagoldfishricardsmasniks.fragments.PlayFragment;

public class PlayActivity extends SingleFragmentActivity
{
    @Override
    protected Fragment createFragment() {
        return PlayFragment.newInstance();
    }
}