package com.seffyo.kandaapptesting.activities.fragment.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.seffyo.kandaapptesting.activities.fragment.fragments.MondayFragment;
import com.seffyo.kandaapptesting.activities.fragment.fragments.TuesdayFragment;
import com.seffyo.kandaapptesting.activities.fragment.fragments.WednesdayFragment;

/**
 * Created by renkar on 10.10.2017.
 */

public class SimpleFragmentPagerAdapter extends FragmentPagerAdapter {

    public SimpleFragmentPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    private String tabTitles[] = new String[] { "Today", "Tomorrow", "The day after tomorrow" };

    @Override
    public Fragment getItem(int position) {
        if (position == 0) {
            return new MondayFragment();
        } else if (position == 1){
            return new TuesdayFragment();
        } else {
            return new WednesdayFragment();
        }
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tabTitles[position];
    }
}
