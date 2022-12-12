package com.example.memoryofagoldfishricardsmasniks.activities;


import androidx.fragment.app.Fragment;

import com.example.memoryofagoldfishricardsmasniks.fragments.MainMenuFragment;

public class MainMenuActivity extends SingleFragmentActivity
{
    @Override
    protected Fragment createFragment()
    {
        return  MainMenuFragment.newInstance();
    }
}
