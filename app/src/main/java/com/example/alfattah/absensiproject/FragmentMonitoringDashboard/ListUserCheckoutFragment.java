package com.example.alfattah.absensiproject.FragmentMonitoringDashboard;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.alfattah.absensiproject.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class ListUserCheckoutFragment extends Fragment {


    public ListUserCheckoutFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_list_user_checkout, container, false);
    }

}
