package com.excilys.voisinsenor.ui.home;

import android.app.Fragment;
import android.app.FragmentManager;
import android.support.v13.app.FragmentPagerAdapter;

/**
 * Created by mada on 28/09/15.
 */
public class DetailsPagerAdapter extends FragmentPagerAdapter {

    TrackFragment trackFragment;
    CourseFragment courseFragment;
    AideFragment aideFragment;

    public DetailsPagerAdapter(FragmentManager fm) {
        super(fm);
        trackFragment = new TrackFragment();
        courseFragment = new CourseFragment();
        aideFragment = new AideFragment();
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return trackFragment;
            case 1:
                return courseFragment;
            case 2:
                return aideFragment;
        }

        return null;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "Trajet";
            case 1:
                return "Course";
            case 2:
                return "Aide";
        }
        return null;
    }

    @Override
    public int getCount() {
        return 3;
    }
}
