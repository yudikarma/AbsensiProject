package com.example.alfattah.absensiproject.fragment_mainActivity;


import android.os.Bundle;
import android.support.annotation.Nullable;
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

/**
 * A simple {@link Fragment} subclass.
 */
public class DashboardMonitoringFragment extends Fragment implements OnMapReadyCallback,ClusterItem {


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
    private LatLng markerlocation;
    MarkerOptions marker;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootview = inflater.inflate(R.layout.fragment_dashboard_monitoring, container, false);
        listUserCheckINFragment = new ListUserCheckINFragment();
        listUserCheckINFragment = new ListUserCheckINFragment();
        viewPager_monitoring = rootview.findViewById(R.id.viewpager_monitoring);

        //Pageviewadapter
        dashboardPageviewAdapter = new AdapterMonitoringDashboard(getActivity().getSupportFragmentManager());
        viewPager_monitoring.setAdapter(dashboardPageviewAdapter);
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
        });

        // Gets the MapView from the XML layout and creates it
        mapView = (MapView) rootview.findViewById(R.id.mapview_monitoring);
        mapView.onCreate(savedInstanceState);

        // Gets to GoogleMap from the MapView and does initialization stuff
        mapView.getMapAsync(this);


        MapsInitializer.initialize(this.getActivity());
        // Updates the location and zoom of the MapView



        return rootview;
    }

    private void settabs(int position) {
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
    }

    @Override
    public void onMapReady(final GoogleMap googleMap) {
        // latitude and longitude
        final double latitude = 17.385044;
        final double longitude = 78.486671;
        FirebaseDatabase.getInstance().getReference().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                DataSnapshot dataSnapshot1 = dataSnapshot.child("Checkin");
                Iterable<DataSnapshot> checkinchildren = dataSnapshot1.getChildren();
                for (DataSnapshot datasnapshotchekinchildren : checkinchildren){
                    ChekinModel chekinModel = datasnapshotchekinchildren.getValue(ChekinModel.class);
                    Log.e("latitude :", chekinModel.getLatitude());
                    listlatitude.add(chekinModel.getLatitude());
                    // create marker
                    lat = Double.parseDouble(chekinModel.getLatitude());
                    lng = Double.parseDouble(chekinModel.getLatitude());
                    String title = chekinModel.getTime();
                     /*zoom = googleMap.getCameraPosition().zoom;*/


                    marker = new MarkerOptions().position(
                            new LatLng(lat, lng)).title(title);

                    // Changing marker icon
                    marker.icon(BitmapDescriptorFactory
                            .defaultMarker(BitmapDescriptorFactory.HUE_RED));

                    // adding marker
                    googleMap.addMarker(marker);




                }
                CameraPosition cameraPosition = new CameraPosition.Builder()
                        .target(new LatLng(lat,lng))
                        .zoom(20)
                        .bearing(90)
                        .tilt(30)
                        .build();
                googleMap.setMinZoomPreference(6f);
                googleMap.setMaxZoomPreference(20f);
                googleMap.animateCamera(CameraUpdateFactory
                        .newCameraPosition(cameraPosition));

                mapView.onResume();




            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        mapView.onResume();
        /*for (int i = 0; i < listlatitude.size(); i++) {
            Log.e("latitude after loop :", ""+ listlatitude.get(i));

        }
*/



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
    public double getLatitude() {
        return 0;
    }

    @Override
    public double getLongitude() {
        return 0;
    }

    @Nullable
    @Override
    public String getTitle() {
        return null;
    }

    @Nullable
    @Override
    public String getSnippet() {
        return null;
    }
}
