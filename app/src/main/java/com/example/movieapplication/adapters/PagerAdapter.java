package com.example.movieapplication.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.example.movieapplication.fragments.LatestFragment;
import com.example.movieapplication.fragments.PopularMoviesFragment;
import com.example.movieapplication.fragments.TopFragment;

public class PagerAdapter extends FragmentStatePagerAdapter {

    int mNumOfTabs;

    public PagerAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
        this.mNumOfTabs = behavior;
    }

    @NonNull
    @Override//getting tabs in position
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                PopularMoviesFragment tab1 = new PopularMoviesFragment();
                return tab1;
            case 1:
                TopFragment tab2 = new TopFragment();
                return tab2;
            case 2:
                LatestFragment tab3 = new LatestFragment();
                return tab3;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}
