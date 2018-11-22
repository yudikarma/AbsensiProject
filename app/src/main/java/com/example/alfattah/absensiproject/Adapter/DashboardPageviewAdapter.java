package com.example.alfattah.absensiproject.Adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.alfattah.absensiproject.Fragment_Dashboard_Staff.CheckOutFragment;
import com.example.alfattah.absensiproject.Fragment_Dashboard_Staff.CheckinFragment;
import com.example.alfattah.absensiproject.Fragment_Dashboard_Staff.MapsFragment;

public class DashboardPageviewAdapter extends FragmentPagerAdapter {

    public DashboardPageviewAdapter(FragmentManager fm) {
        super(fm);

    }



    //BAGIAN ATUR FRAGMENT ON TAB SELECTION
    @Override
    public Fragment getItem(int position) {
        switch(position) {
            case 0:
                return new CheckinFragment();

            case 1:
                return new MapsFragment();

            case 2:
                return new CheckOutFragment();

            default:
                return  null;
        }
    }

    @Override
    public int getCount() {
        return 3;
    }

    //BAGIAN ATUR TEXt TItle TABS
    @Override
    public CharSequence getPageTitle(int position) {
        switch (position){
            case 0:
                return "Check IN";
            case 1:
                return "Maps";
            case 2:
                return "Check Out";
            default:
                return null;
        }

    }

}
