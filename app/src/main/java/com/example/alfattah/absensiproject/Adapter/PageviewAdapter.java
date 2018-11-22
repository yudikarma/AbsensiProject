package com.example.alfattah.absensiproject.Adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.alfattah.absensiproject.fragment_HomeActivity.LoginFragment;
import com.example.alfattah.absensiproject.fragment_HomeActivity.RegisterFragment;
import com.example.alfattah.absensiproject.fragment_HomeActivity.ResetpassFragment;


public class PageviewAdapter extends FragmentPagerAdapter {
    public PageviewAdapter(FragmentManager fm) {
        super(fm);
    }

    //BAGIAN ATUR FRAGMENT ON TAB SELECTION
    @Override
    public Fragment getItem(int position) {
        switch(position) {
            case 0:
                LoginFragment loginFragment = new LoginFragment();
                return loginFragment;

            case 1:
                RegisterFragment registerFragment = new RegisterFragment();
                return registerFragment;

            case 2:
                ResetpassFragment resetpassFragment = new ResetpassFragment();
                return resetpassFragment;

            default:
                return  null;
        }
    }


    @Override
    public int getCount() {
        return 3;
    }
}
