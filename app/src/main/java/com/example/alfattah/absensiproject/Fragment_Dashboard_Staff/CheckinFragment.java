package com.example.alfattah.absensiproject.Fragment_Dashboard_Staff;


import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.alfattah.absensiproject.R;
import com.example.alfattah.absensiproject.map_Helper.DirectionFinderListener;
import com.example.alfattah.absensiproject.map_Helper.Route;
import com.example.alfattah.absensiproject.utils.PreferenceManager;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import id.zelory.compressor.Compressor;

import static android.app.Activity.RESULT_OK;


/**
 * A simple {@link Fragment} subclass.
 */
public class CheckinFragment extends Fragment {


    private String nama;

    public CheckinFragment() {
        // Required empty public constructor
    }

    private TextView notifikasi, ketnotifikasi;
    /*
        private PreferenceManager preferenceManager;*/
    private Calendar waktuskrng, waktumulaikerja, waktuakhirkerja;
    String today, status, snotifikasi, sketnotifikasi;
    private FloatingActionButton floatingActionButton;

    private int REQUEST_IMAGE_CAPTURE = 1;

    //VARIABEL UNTUK TAKE PICTURE
    private Uri imageUri;
    private Uri newsImageUri;
    private File photofile = null;
    private File thumb_file;


    //TO GET ADDRESS FROM CURRENT LOCATION
    Geocoder geocoder;
    List<Address> addresses;
    String origin;
    float latorigin, lngorigin;


    //get lat lng from current location
    static final int REQUEST_LOCATION = 1;
    LocationManager locationManager;

    private StorageReference mStorageReference;
    private FirebaseUser mCurrentuser;
    private ProgressDialog mProgressDialog;
    private DatabaseReference mDatabaseReference;
    private DatabaseReference mcheckinReference;
    private DatabaseReference mcheckoutReference;
    private String uid;
    private LatLng latLng;
    private double latitude, longitude;
    private float kilometer;
    String distance;
    public static final int LOCATION_UPDATE_MIN_DISTANCE = 10;
    public static final int LOCATION_UPDATE_MIN_TIME = 5000;
    private LocationManager mLocationManager;
    double latitudedestination ;
    double longitudedestination ;
    double getmeter ;
    String islogin ;

    private LocationListener locationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            if (location != null) {
                latitude = location.getLatitude();
                longitude = location.getLongitude();

                mLocationManager.removeUpdates(locationListener);

            } else {
                Log.i("my location :", "IS NULL");
            }
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {

        }
    };


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootview = inflater.inflate(R.layout.fragment_checkin, container, false);
        notifikasi = rootview.findViewById(R.id.notifikasi_chekin);
        ketnotifikasi = rootview.findViewById(R.id.ket_notif);

        floatingActionButton = rootview.findViewById(R.id.fab_chekin);

        /*        preferenceManager = new PreferenceManager(getActivity());*/
        mStorageReference = FirebaseStorage.getInstance().getReference();
        mCurrentuser = FirebaseAuth.getInstance().getCurrentUser();
        mProgressDialog = new ProgressDialog(getActivity());
        uid = mCurrentuser.getUid();
        /*latitude = Double.parseDouble(null);
        longitude = Double.parseDouble(null);*/
        mDatabaseReference = FirebaseDatabase.getInstance().getReference().child("Users").child(uid);
        mDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                nama = ""+dataSnapshot.child("nama").getValue().toString();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        mcheckinReference = FirebaseDatabase.getInstance().getReference().child("Checkin").child(uid);
        mcheckoutReference = FirebaseDatabase.getInstance().getReference();



        mDatabaseReference.keepSynced(true);

        mLocationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        getCurrentLocation();

        //waktu
        waktumulaikerja = Calendar.getInstance();
        waktumulaikerja.setTimeInMillis(System.currentTimeMillis());
        waktumulaikerja.set(Calendar.HOUR_OF_DAY, 8);
        waktumulaikerja.set(Calendar.MINUTE, 0);

        waktuakhirkerja = Calendar.getInstance();
        waktuakhirkerja.setTimeInMillis(System.currentTimeMillis());
        waktuakhirkerja.set(Calendar.HOUR_OF_DAY, 17);
        waktuakhirkerja.set(Calendar.MINUTE, 0);

        waktuskrng = Calendar.getInstance();
        waktuskrng.setTimeInMillis(System.currentTimeMillis());

        today = DateFormat.getDateInstance().format(new Date());

        /*if (!(waktuskrng.getTimeInMillis() > waktuakhirkerja.getTimeInMillis() || waktuskrng.getTimeInMillis() < waktumulaikerja.getTimeInMillis())) {*/
        /*islogin = "true";*/
        mDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                islogin = dataSnapshot.child("checkIN").getValue().toString();
                Log.e("status login :", ""+islogin);
                if (islogin.equals("true")) {
                    //true
                    status = "ischeked";
                    snotifikasi = "You have Checked";
                    sketnotifikasi = "Your current status has been checked. thank you";
                    floatingActionButton.setVisibility(View.GONE);
                    notifikasi.setText(snotifikasi);
                    ketnotifikasi.setText(sketnotifikasi);
                } else {

                    if (waktuskrng.getTimeInMillis() > waktumulaikerja.getTimeInMillis() && waktuskrng.getTimeInMillis() < waktuakhirkerja.getTimeInMillis()) {
                        //bisa check in
                        status = "notyetcheckin";
                        snotifikasi = "You haven't checked yet";
                        sketnotifikasi = "You haven't checked yet, please click the button below to check";
                        floatingActionButton.setVisibility(View.VISIBLE);
                        notifikasi.setText(snotifikasi);
                        ketnotifikasi.setText(sketnotifikasi);


                    } else {
                        //gak bisa chekin
                        status = "cantchekin";
                        snotifikasi = "Can't Chek In";
                        sketnotifikasi = "cannot check in at this time because it is not yet time";
                        floatingActionButton.setVisibility(View.GONE);
                        notifikasi.setText(snotifikasi);
                        ketnotifikasi.setText(sketnotifikasi);
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        /*}else {
            status = "notyettime";
            snotifikasi = ""
            notifikasi.setText(snotifikasi);
            ketnotifikasi.setText(sketnotifikasi);


        }*/

        Log.i("chek notif", "" + snotifikasi + " " + sketnotifikasi);
        notifikasi.setText(snotifikasi);
        ketnotifikasi.setText(sketnotifikasi);


        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doCheckin();
            }
        });


        return rootview;
    }

    private void getCurrentLocation() {
        boolean isGPSEnabled = mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        boolean isNetworkEnabled = mLocationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

        Location location = null;
        if (!(isGPSEnabled || isNetworkEnabled))
            /* Snackbar.make(mMapView, R.string.error_location_provider, Snackbar.LENGTH_INDEFINITE).show();*/
            Toast.makeText(getActivity(), "Error location provider" , Toast.LENGTH_SHORT);

        else {
            if (isNetworkEnabled) {
                if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                mLocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,
                        LOCATION_UPDATE_MIN_TIME, LOCATION_UPDATE_MIN_DISTANCE, locationListener);
                location = mLocationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            }

            if (isGPSEnabled) {
                mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                        LOCATION_UPDATE_MIN_TIME, LOCATION_UPDATE_MIN_DISTANCE, locationListener);
                location = mLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            }
        }
        if (location != null) {
            /*Logger.d(String.format("getCurrentLocation(%f, %f)", location.getLatitude(),
                    location.getLongitude()));*/
            latitude = location.getLatitude();
            longitude = location.getLongitude();/*
            drawMarker(location);*/
        }
    }

    private void doCheckin() {
        //if status not yet time
        if (status.equalsIgnoreCase("notyettime")){

        }
        if (status.equalsIgnoreCase("ischeked")){

        }
        if (status.equalsIgnoreCase("cantchekin")){

        }
        if (status.equalsIgnoreCase("notyetcheckin")){
            //CHEK PERMISSION ACCES CAMERA
            if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA)
                    == PackageManager.PERMISSION_DENIED) {

                //Meminta Akses Camera
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CAMERA}, REQUEST_IMAGE_CAPTURE);

            }
            //CHEK Permission MENYIMPAN FILE (Storage)
            else if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_DENIED) {

                //MEMINTA ACCES
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_IMAGE_CAPTURE);
            } else {


                /* ==========Jika AKses diberikan, maka penggil Intent Camera =========*/
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {

                    //Generate Name
                    String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
                    photofile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), timeStamp + "anu.jpg");

                    //Chek photofile tidak null;
                    if (photofile != null) {

                        //Mendapatkan Alamat URi dari foto File
                        imageUri = Uri.fromFile(photofile);

                        //Mengirim ALamat Uri
                        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);

                        //Memanggil Activity Onresult dari Camera Intent
                        startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);


                    } else {
                        Toast.makeText(getActivity(), "PHOTO FILE NULL", Toast.LENGTH_LONG).show();
                    }

                }
            }



        }
        //if status not checkin
        //if status was check in
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //Jika Request nya ImageCapture(Camera) dan user menekan OK (Selesai mengambil foto)
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {

            mProgressDialog.setTitle("Upload Selfie chek in");
            mProgressDialog.setMessage("we wil try to upload your images");
            mProgressDialog.setCanceledOnTouchOutside(false);
            mProgressDialog.show();

            FirebaseDatabase.getInstance().getReference().child("DataPerusahaan").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    latitudedestination = Double.parseDouble(dataSnapshot.child("latitude").getValue().toString());
                    longitudedestination = Double.parseDouble(dataSnapshot.child("longitude").getValue().toString());
                    float results[] = new float[10];
                    Location.distanceBetween(latitude, longitude, latitudedestination, longitudedestination, results);
                    distance = ""+results[0];


                    /* double distancetest = Double.parseDouble(distance)/1000;*/
                    getmeter = Math.floor(Double.parseDouble(distance));
                    Toast.makeText(getActivity(), ""+getmeter, Toast.LENGTH_SHORT).show();
                    if (getmeter <= 100){
                        if (imageUri != null){
                            try {

                                //Kompress File asal kedalam File Baru dengan ukuran lebih ringan
                                File newsfile = new Compressor(getActivity())
                                        .compressToFile(photofile);

                                //Mendapatkan Uri file baru setelah di kompress
                                newsImageUri = Uri.fromFile(newsfile);

                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            //Mendapatkan hasil generate  ID
                            final String push_id = mcheckinReference.getKey();


                            //Path penyimpanan FILE Pada FIrebas Storage
                            final StorageReference filepath = mStorageReference.child("chekin_selfie").child(push_id+uid+ ".jpg");
                            filepath.putFile(newsImageUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                                    if (task.isSuccessful()) {
                                        //Toast.makeText(SettingActivity.this,"working",Toast.LENGTH_LONG).show();

                                        filepath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                            @Override
                                            public void onSuccess(Uri uri) {

                                                /*final  Uri downloadUri = uri;*/
                                                final String currenttime = DateFormat.getTimeInstance().format(new Date());
                                                final String sdownloadUri = uri.toString();
                                                Map update_hasmap = new HashMap();
                                                update_hasmap.put("image",""+sdownloadUri);
                                                update_hasmap.put("uid",""+ uid);
                                                update_hasmap.put("nama",""+ nama);
                                                update_hasmap.put("latitude",""+ latitude);
                                                update_hasmap.put("longitude",""+ longitude);
                                                update_hasmap.put("distance",""+ getmeter);
                                                update_hasmap.put("timestamp",ServerValue.TIMESTAMP);
                                                update_hasmap.put("time", ""+currenttime);




                                                mcheckinReference.updateChildren(update_hasmap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull final Task<Void> task) {
                                                        if (task.isSuccessful()) {

                                                            Map deletecheckout = new HashMap();
                                                            deletecheckout.put("checkout/"+uid, null);

                                                            mcheckoutReference.updateChildren(deletecheckout, new DatabaseReference.CompletionListener() {
                                                                @Override
                                                                public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                                                                    mProgressDialog.dismiss();
                                                                    /*preferenceManager.setsudahChekin(true);*/
                                                                    mDatabaseReference.child("checkIN").setValue("true");
                                                                    status = "ischeked";
                                                                    snotifikasi = "You have Checked";
                                                                    sketnotifikasi = "Your current status has been checked. thank you";
                                                                    floatingActionButton.setVisibility(View.GONE);
                                                                    notifikasi.setText(snotifikasi);
                                                                    ketnotifikasi.setText(sketnotifikasi);

                                                                    Toast.makeText(getActivity(), "Succes Melakukan Check in, Terima Kasih", Toast.LENGTH_LONG).show();

                                                                }
                                                            });



                                                        }
                                                        else {
                                                            mProgressDialog.dismiss();
                                                            Toast.makeText(getActivity(), "Gagal melakukan check in, please try again", Toast.LENGTH_LONG).show();
                                                        }
                                                    }
                                                });

                                            }
                                        }) ;

                                    } else {
                                        mProgressDialog.dismiss();
                                        Toast.makeText(getActivity(), "Error upload", Toast.LENGTH_LONG).show();
                                        mProgressDialog.dismiss();
                                    }

                                }
                            });



                        }
                    }else {
                        mProgressDialog.dismiss();
                        Toast.makeText(getActivity(), "Sorry, you are out of Range", Toast.LENGTH_SHORT).show();
                    }
/*                    Toast.makeText(getActivity(), ""+distance, Toast.LENGTH_SHORT).show();
                    Toast.makeText(getActivity(), ""+distancetest, Toast.LENGTH_SHORT).show();
                    Log.i("distance", ""+distance);
                    Log.i("distance", ""+distancetest);*/




                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        } else {
            mProgressDialog.dismiss();
            Toast.makeText(getActivity(), "URI IS NULL", Toast.LENGTH_LONG).show();
        }

    }








    @Override
    public void onStart() {
        super.onStart();





    }

}
