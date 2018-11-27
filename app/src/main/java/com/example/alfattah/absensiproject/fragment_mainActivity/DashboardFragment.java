package com.example.alfattah.absensiproject.fragment_mainActivity;


import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.alfattah.absensiproject.Adapter.DashboardPageviewAdapter;
import com.example.alfattah.absensiproject.Fragment_Dashboard_Staff.CheckOutFragment;
import com.example.alfattah.absensiproject.Fragment_Dashboard_Staff.CheckinFragment;
import com.example.alfattah.absensiproject.Fragment_Dashboard_Staff.MapsFragment;
import com.example.alfattah.absensiproject.R;
import com.example.alfattah.absensiproject.utils.PreferenceManager;

import java.util.Objects;



/**
 * A simple {@link Fragment} subclass.
 */
public class DashboardFragment extends Fragment {


    public DashboardFragment() {
        // Required empty public constructor
    }



    CheckinFragment checkinFragment = new CheckinFragment();
    MapsFragment mapsFragment = new MapsFragment();
    CheckOutFragment checkOutFragment = new CheckOutFragment();
    ViewPager dashboardViewpager;
    PreferenceManager preferenceManager;

    private DashboardPageviewAdapter dashboardPageviewAdapter;
    int currentitem ;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View  view =  inflater.inflate(R.layout.fragment_dashboard, container, false);
        dashboardPageviewAdapter = new DashboardPageviewAdapter(getChildFragmentManager());
        dashboardViewpager = view.findViewById(R.id.viewPagerdashboardstaff);
        dashboardViewpager.setAdapter(dashboardPageviewAdapter);

        preferenceManager = new PreferenceManager(Objects.requireNonNull(getActivity()));

        checkstatuschekin();




        TabLayout tabLayout = view.findViewById(R.id.tabs);
        dashboardViewpager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(dashboardViewpager));

        currentitem = dashboardViewpager.getCurrentItem();
        if (currentitem == 0){
           /* tabLayout.setVisibility(View.GONE);*/
        }else if (currentitem == 2){
            /*tabLayout.setVisibility(View.GONE);*/
        }


        return view;
    }

    private void checkstatuschekin() {
        if (!preferenceManager.getsudahcheckin()){
            //belum check in
            dashboardViewpager.setCurrentItem(0);
        }else {
            //sudah check in
            dashboardViewpager.setCurrentItem(1);

        }
    }




}
