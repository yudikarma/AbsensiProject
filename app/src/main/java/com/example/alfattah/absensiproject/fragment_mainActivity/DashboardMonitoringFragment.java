package com.example.alfattah.absensiproject.fragment_mainActivity;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.alfattah.absensiproject.Adapter.AdapterMonitoringDashboard;
import com.example.alfattah.absensiproject.Adapter.DashboardPageviewAdapter;
import com.example.alfattah.absensiproject.FragmentMonitoringDashboard.ListUserCheckINFragment;
import com.example.alfattah.absensiproject.FragmentMonitoringDashboard.ListUserCheckoutFragment;
import com.example.alfattah.absensiproject.Model.ChekinModel;
import com.example.alfattah.absensiproject.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import net.sharewire.googlemapsclustering.ClusterItem;

import java.util.ArrayList;
import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 */
public class DashboardMonitoringFragment extends Fragment implements OnMapReadyCallback {


    public DashboardMonitoringFragment() {
        // Required empty public constructor
    }

    MapView mapView;
    GoogleMap map;
    private ListUserCheckoutFragment listUserCheckoutFragment;
    private ListUserCheckINFragment listUserCheckINFragment;
    private AdapterMonitoringDashboard dashboardPageviewAdapter;
    private ViewPager viewPager_monitoring;
    ArrayList<String> listlatitude = new ArrayList<>();

    double lat;
    double lng ;
    String time ;
    float zoom;
    private GoogleMap googleMap;
    private MarkerOptions options = new MarkerOptions();
    private ArrayList<LatLng> latlngs = new ArrayList<>();
    private LatLng markerlocation;
    MarkerOptions marker;
    private String title;
    private String uid ;
    private String nama;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootview = inflater.inflate(R.layout.fragment_dashboard_monitoring, container, false);
        listUserCheckINFragment = new ListUserCheckINFragment();
        listUserCheckINFragment = new ListUserCheckINFragment();
        //Pageviewadapter
        dashboardPageviewAdapter = new AdapterMonitoringDashboard(getChildFragmentManager());
        viewPager_monitoring = rootview.findViewById(R.id.viewpager_monitoring);
        viewPager_monitoring.setAdapter(dashboardPageviewAdapter);

        //Tablayout
        TabLayout tabLayout = rootview.findViewById(R.id.tabs);

        viewPager_monitoring.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(viewPager_monitoring));


       /* viewPager_monitoring.setAdapter(dashboardPageviewAdapter);*//*
        viewPager_monitoring.setCurrentItem(0);

        viewPager_monitoring.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                settabs(position);

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });*/

        // Gets the MapView from the XML layout and creates it
        mapView = (MapView) rootview.findViewById(R.id.mapview_monitoring);
        mapView.onCreate(savedInstanceState);

        // Gets to GoogleMap from the MapView and does initialization stuff
        mapView.getMapAsync(this);


        MapsInitializer.initialize(getActivity());
        // Updates the location and zoom of the MapView



        return rootview;
    }

    /*private void settabs(int position) {
        switch (position){
            case 0:
                viewPager_monitoring.setCurrentItem(0);
                break;
            case 1:
                viewPager_monitoring.setCurrentItem(1);
                break;
            default:
                break;
        }
    }*/

    @Override
    public void onMapReady(final GoogleMap googleMap) {
        // latitude and longitude

        FirebaseDatabase.getInstance().getReference().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                DataSnapshot dataSnapshot1 = dataSnapshot.child("Checkin");
                Iterable<DataSnapshot> checkinchildren = dataSnapshot1.getChildren();
                for (DataSnapshot datasnapshotchekinchildren : checkinchildren){
                    ChekinModel chekinModel = datasnapshotchekinchildren.getValue(ChekinModel.class);
                    Log.e("latitude :", chekinModel.getLatitude());
                    /*listlatitude.add(chekinModel.getLatitude());*/
                    // create marker
                    lat = Double.parseDouble(chekinModel.getLatitude());
                    lng = Double.parseDouble(chekinModel.getLongitude());
                     title = chekinModel.getTime();
                    uid = chekinModel.getUid();
                    nama = chekinModel.getNama();

                    latlngs.add(new LatLng(lat,lng));
                    for (LatLng points : latlngs){
                        options.position(points);
                        options.title(""+nama+" at : "+title);
                        options.icon(BitmapDescriptorFactory
                                .defaultMarker(BitmapDescriptorFactory.HUE_RED));

                        googleMap.addMarker(options);

                    }
                    CameraPosition cameraPosition = new CameraPosition.Builder()
                            .target(new LatLng(lat, lng)).zoom(15).build();
                    googleMap.animateCamera(CameraUpdateFactory
                            .newCameraPosition(cameraPosition));

                    MapsInitializer.initialize(Objects.requireNonNull(getActivity()));
                    mapView.onResume();









                }


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }
    @Override
    public void onResume() {

        mapView.onResume();
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    @Override
    public void onDetach() {
        super.onDetach();

    }

    @Override
    public void onStart() {
        super.onStart();

    }
}
