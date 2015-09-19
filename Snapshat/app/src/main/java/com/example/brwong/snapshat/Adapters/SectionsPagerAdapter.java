package com.example.brwong.snapshat.Adapters;

/**
 * Created by Brwong on 5/26/15.
 */

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.brwong.snapshat.Fragments.FriendsFragment;
import com.example.brwong.snapshat.Fragments.InboxFragment;
import com.example.brwong.snapshat.R;

import java.util.Locale;

/**
 * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
public class SectionsPagerAdapter extends FragmentPagerAdapter {

    protected Context mContext;//Since this class isn't in an activity anymore, WE NEED A CONTEXT

    public SectionsPagerAdapter(Context context, FragmentManager fm) {
        super(fm);
        mContext = context;
    }

    @Override
    public Fragment getItem(int position) {//Fragments are grouped like arrays, that's why we use ints as their index
        // getItem is called to instantiate the fragment for the given page.
        // Return a PlaceholderFragment (defined as a static inner class below).
        switch (position){
            case 0:
                return new InboxFragment();
            case 1:
                return new FriendsFragment();
        }
        return null;
    }

    @Override
    public int getCount() {//Controls how many tabs to display
        // Show 2 total pages.
        return 2;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        Locale l = Locale.getDefault();
        switch (position) {//Gives a title to each fragment based on position
            case 0:
                return mContext.getString(R.string.title_section1).toUpperCase(l);
            case 1:
                return mContext.getString(R.string.title_section2).toUpperCase(l);

        }
        return null;
    }

    public int getIcon(int position){
        switch(position){
            case 0:
                return R.drawable.ic_tab_inbox;
            case 1:
                return R.drawable.ic_tab_friends;
            default:
                return R.drawable.ic_tab_inbox;
        }
    }
}
