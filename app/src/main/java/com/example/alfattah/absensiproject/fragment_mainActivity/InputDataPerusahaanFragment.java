package com.example.alfattah.absensiproject.fragment_mainActivity;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatRatingBar;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.alfattah.absensiproject.HomeActivity;
import com.example.alfattah.absensiproject.MainActivity;
import com.example.alfattah.absensiproject.R;
import com.example.alfattah.absensiproject.utils.PreferenceManager;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.xmlpull.v1.XmlPullParser;

import java.util.HashMap;
import java.util.Map;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 */
public class InputDataPerusahaanFragment extends Fragment {


    private BottomSheetBehavior bottomSheetBehavior;
    private Place place_Picker;
    private DatabaseReference DataPerusahaan;
    /*private PreferenceManager preferenceManager;*/

    private static int Place_Picker_Request = 1;
    private static int MY_PERMISSION_FINE_LOCATION = 1;
    LatLng latLng ;
    LatLng latLng2;
    float lat;
    float longitude;
    private  Context context;

    private InputDataPerusahaanFragment inputDataPerusahaanFragment;




    //view
    AppCompatEditText cmpnType,cmpnName,cmpnPhone,cmpnEmail,cmpnAddress,cmpnwebsite;
    AppCompatRatingBar ratingBar;
    TextView txtrating,txtlatlng,txtaddresplace,txtphoneplaces,txtjamoperasiplace;
    String scmpnType,scmpnName,scmpnPhone,scmpnEmail,scmpnAddress,sratingBar,stxtrating,stxtlatlng,stxtaddresplace,stxtphoneplaces,stxtjamoperasiplace,scmpnwebsite;


    public InputDataPerusahaanFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootview = inflater.inflate(R.layout.fragment_input_data_perusahaan, container, false);

            //prefmanager
       /* preferenceManager = new PreferenceManager(getActivity());*/
        //init toolbar
        Toolbar toolbar = (Toolbar) rootview.findViewById(R.id.toolbar);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Biodata Perusahaan");
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setHasOptionsMenu(true);
        DataPerusahaan = FirebaseDatabase.getInstance().getReference();






        //init bootom sheet
        LinearLayout llBottomSheet = (LinearLayout) rootview.findViewById(R.id.bottom_sheet);


        bottomSheetBehavior = BottomSheetBehavior.from(llBottomSheet);

        // change the state of the bottom sheet
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        cmpnType = rootview.findViewById(R.id.businis_category_e);
        cmpnName = rootview.findViewById(R.id.company_name_e);
        cmpnPhone= rootview.findViewById(R.id.phone_number_e);
        cmpnEmail = rootview.findViewById(R.id.email_e);
        cmpnAddress = rootview.findViewById(R.id.adddress_e);
        cmpnwebsite = rootview.findViewById(R.id.website_e);

        //from google plaaces
        ratingBar = rootview.findViewById(R.id.ratingperusahaan);
        txtrating = rootview.findViewById(R.id.ratingperusahaan_text);
        txtlatlng = rootview.findViewById(R.id.ltlang_place);
        txtaddresplace = rootview.findViewById(R.id.address_places);
        txtphoneplaces = rootview.findViewById(R.id.phone_number_place);
        txtjamoperasiplace = rootview.findViewById(R.id.jamberoperasi_places);

        //getstring

        cmpnType.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                scmpnType = s.toString();
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        cmpnName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                scmpnName = s.toString();
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        cmpnPhone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                scmpnPhone = s.toString();
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        cmpnEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                scmpnEmail = s.toString();
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        cmpnAddress.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if (s!= null){
                    scmpnAddress = s.toString();
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        cmpnwebsite.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if (s != null){
                    scmpnwebsite = s.toString();
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });




        // set callback for changes
        bottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {

            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {

            }
        });

        ((FloatingActionButton) rootview.findViewById(R.id.fab_directions)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    PlacePicker.IntentBuilder intentBuilder = new PlacePicker.IntentBuilder();
                    /*Intent intent = intentBuilder.build(getActivity());*/
                    // Start the Intent by requesting a result, identified by a request code.
                    startActivityForResult(intentBuilder.build(getActivity()),5);

                    // Hide the pick option in the UI to prevent users from starting the picker
                    // multiple times.


                } catch (GooglePlayServicesRepairableException e) {
                    GooglePlayServicesUtil
                            .getErrorDialog(e.getConnectionStatusCode(), getActivity(), 0);
                } catch (GooglePlayServicesNotAvailableException e) {
                    Toast.makeText(getActivity(), "Google Play Services is not available.",
                            Toast.LENGTH_LONG)
                            .show();
                }

                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);

                /*try {
                    mMap.animateCamera(zoomingLocation());
                } catch (Exception e) {
                }*/
            }
        });




        return rootview;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.i("masuk onresult", "onresult");
        Log.i("result   ", "request code "+requestCode+","+"result"+resultCode+"data"+data);
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 5 && resultCode == RESULT_OK){


               place_Picker = PlacePicker.getPlace(getActivity(), data);

                stxtrating = String.format("%s", place_Picker.getRating());
                stxtaddresplace = String.format("%s", place_Picker.getAddress().toString());
                stxtphoneplaces = String.format("%s", place_Picker.getPhoneNumber().toString());
                stxtjamoperasiplace = String.format("%s", place_Picker.getName().toString());
                stxtlatlng = String.format("%s", place_Picker.getLatLng());
                 latLng2 = place_Picker.getLatLng();
                 lat = (float) latLng2.latitude;
                 longitude = (float) latLng2.longitude;



               /* if (sratingBar != null){
                    float f = Float.parseFloat(sratingBar);
                    ratingBar.setRating(f);
                }else {

                }*/
                /* float f = Float.parseFloat(sratingBar);
                ratingBar.setRating(f);*/
                txtlatlng.setText(""+stxtlatlng);
                txtaddresplace.setText(""+stxtaddresplace);
                txtphoneplaces.setText(""+stxtphoneplaces);
                txtjamoperasiplace.setText(""+stxtjamoperasiplace);



                Log.i("getdata info : ",""+scmpnType+" "+scmpnName+" "+scmpnPhone+" "+scmpnEmail+" "+scmpnAddress+" "+stxtrating+" "+stxtaddresplace+" "+stxtlatlng+" "+stxtjamoperasiplace+""+latLng2+" "+lat+" "+longitude);


        }

    }

  /*  private void getLatLngFromString(String[] stxtlatlng) {
        double lat = Double.parseDouble(stxtlatlng[0]);
        double lon = Double.parseDouble(stxtlatlng[1]);
        latLng = new LatLng(lat,lon);
    }*/

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_done, menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            Intent startIntent = new Intent(getActivity(), HomeActivity.class);
            startIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(startIntent);

            getActivity().finish();
        }
        if (item.getItemId() == R.id.action_done){
            simpanDataPerusahaan();

        }
        else {
            Toast.makeText(getActivity(), item.getTitle(), Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }

    private void simpanDataPerusahaan() {

        Log.i("getdata info : ",""+scmpnType+" "+scmpnName+" "+scmpnPhone+" "+scmpnEmail+" "+scmpnAddress+" "+stxtrating+" "+stxtaddresplace+" "+stxtlatlng+" "+stxtjamoperasiplace+""+latLng);

        if (!TextUtils.isEmpty(scmpnType) && !TextUtils.isEmpty(scmpnName) && !TextUtils.isEmpty(scmpnPhone) && !TextUtils.isEmpty(scmpnEmail) && !TextUtils.isEmpty(scmpnAddress) && !TextUtils.isEmpty(stxtaddresplace) && !TextUtils.isEmpty(stxtlatlng) && !TextUtils.isEmpty(stxtjamoperasiplace) ){
            HashMap<String,String> save = new HashMap<>();
            save.put("businestype", ""+scmpnType);
            save.put("companyName", ""+scmpnName);
            save.put("companyPhone", ""+scmpnPhone);
            save.put("companyEmail", ""+scmpnEmail);
            save.put("companyAddress", ""+scmpnAddress);
            save.put("companyWebsite", ""+scmpnwebsite);
            save.put("latlngplace", ""+stxtlatlng);
            save.put("addressplace", ""+stxtaddresplace);
            save.put("phoneplace", ""+stxtphoneplaces);
            save.put("nameplace", ""+stxtjamoperasiplace);
            save.put("latlng2", ""+latLng2);
            save.put("latitude", ""+lat);
            save.put("longitude", ""+longitude);
            Map simpandata = new HashMap();
            simpandata.put("DataPerusahaan", save);

            DataPerusahaan.updateChildren(simpandata, new DatabaseReference.CompletionListener() {
                @Override
                public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                    if (databaseError == null){
                        /*Toast.makeText(getActivity(), "Berhasil menyimpan Data perusahaan", Toast.LENGTH_LONG).show();
                        Toast.makeText(getActivity(), "Anda dapat memonitoring absen karyawan dari halaman dashboard..", Toast.LENGTH_LONG).show();*//*
                        /// Background operation returns

                        Intent intent = new Intent(getActivity(),MainActivity.class);
                        *//*intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);*//*
                        startActivity(intent);
                        getActivity().finish();*/
                        getActivity().getSupportFragmentManager().popBackStack();

                    }
                }
            });




        }else {
            Toast.makeText(getActivity(), "Data Tidak boleh kosong", Toast.LENGTH_LONG).show();
        }


    }


}
