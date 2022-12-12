package com.example.memoryofagoldfishricardsmasniks.activities;

import androidx.fragment.app.Fragment;

import com.example.memoryofagoldfishricardsmasniks.fragments.ThemeFragment;
import com.example.memoryofagoldfishricardsmasniks.volley.AcceptSSLCerts;

public class ThemeActivity extends SingleFragmentActivity
{
    @Override
    protected Fragment createFragment()
    {
        AcceptSSLCerts.accept();
        return ThemeFragment.newInstance();
    }
}
