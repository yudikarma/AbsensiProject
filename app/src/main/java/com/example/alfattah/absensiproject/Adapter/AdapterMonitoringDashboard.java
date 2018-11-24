package com.example.alfattah.absensiproject.Adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.alfattah.absensiproject.FragmentMonitoringDashboard.ListUserCheckINFragment;
import com.example.alfattah.absensiproject.FragmentMonitoringDashboard.ListUserCheckoutFragment;
import com.example.alfattah.absensiproject.fragment_HomeActivity.LoginFragment;
import com.example.alfattah.absensiproject.fragment_HomeActivity.RegisterFragment;
import com.example.alfattah.absensiproject.fragment_HomeActivity.ResetpassFragment;


public class AdapterMonitoringDashboard extends FragmentPagerAdapter {
    public AdapterMonitoringDashboard(FragmentManager fm) {
        super(fm);
    }

    //BAGIAN ATUR FRAGMENT ON TAB SELECTION
    @Override
    public Fragment getItem(int position) {
        switch(position) {
            case 0:
                ListUserCheckINFragment listUserCheckINFragment = new ListUserCheckINFragment();
                return listUserCheckINFragment;

            case 1:
                ListUserCheckoutFragment listUserCheckoutFragment = new ListUserCheckoutFragment();
                return listUserCheckoutFragment;



            default:
                return  null;
        }
    }


    @Override
    public int getCount() {
        return 2;
    }

    //BAGIAN ATUR TEXt TItle TABS
    @Override
    public CharSequence getPageTitle(int position) {
        switch (position){
            case 0:
                return "User Check in";
            case 1:
                return "User Check Out";

            default:
                return null;
        }

    }
}
