package com.example.alfattah.absensiproject.Fragment_Dashboard_Staff;


import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
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
public class CheckinFragment extends Fragment  {


    public CheckinFragment() {
        // Required empty public constructor
    }

    private TextView notifikasi,ketnotifikasi;

    private PreferenceManager preferenceManager;
    private Calendar waktuskrng, waktumulaikerja   ,waktuakhirkerja ;
    String today, status,snotifikasi,sketnotifikasi;
    private FloatingActionButton floatingActionButton;

    private int REQUEST_IMAGE_CAPTURE = 1;

    //VARIABEL UNTUK TAKE PICTURE
    private Uri imageUri;
    private Uri newsImageUri;
    private File photofile = null;
    private File thumb_file;

    // Storage Firebase
    private StorageReference mImageStorage;

    //TO GET ADDRESS FROM CURRENT LOCATION
    Geocoder geocoder;
    List<Address> addresses;
    String origin;
    float latorigin,lngorigin;


    //get lat lng from current location
    static final int REQUEST_LOCATION = 1;
    LocationManager locationManager;

    private StorageReference mStorageReference;
    private FirebaseUser mCurrentuser;
    private ProgressDialog mProgressDialog;
    private DatabaseReference mDatabaseReference;
    private DatabaseReference mcheckinReference;
    private String uid ;
    private LatLng latLng;
    private double latitude,longitude;
    private float kilometer;
    String distance;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootview = inflater.inflate(R.layout.fragment_checkin, container, false);
        notifikasi = rootview.findViewById(R.id.notifikasi_chekin);
        ketnotifikasi = rootview.findViewById(R.id.ket_notif);

        floatingActionButton = rootview.findViewById(R.id.fab_chekin);

        preferenceManager  = new PreferenceManager(getActivity());
        mStorageReference = FirebaseStorage.getInstance().getReference();
        mCurrentuser = FirebaseAuth.getInstance().getCurrentUser();
        mProgressDialog = new ProgressDialog(getActivity());
        uid = mCurrentuser.getUid();
        /*latitude = Double.parseDouble(null);
        longitude = Double.parseDouble(null);*/
        mDatabaseReference = FirebaseDatabase.getInstance().getReference().child("Users").child(uid);
        mcheckinReference = FirebaseDatabase.getInstance().getReference().child("Checkin").child(uid);

        mDatabaseReference.keepSynced(true);

        //waktu
        waktumulaikerja = Calendar.getInstance();
        waktumulaikerja.setTimeInMillis(System.currentTimeMillis());
        waktumulaikerja.set(Calendar.HOUR_OF_DAY, 7);
        waktumulaikerja.set(Calendar.MINUTE, 0);

        waktuakhirkerja = Calendar.getInstance();
        waktuakhirkerja.setTimeInMillis(System.currentTimeMillis());
        waktuakhirkerja.set(Calendar.HOUR_OF_DAY, 19);
        waktuakhirkerja.set(Calendar.MINUTE, 0);

        waktuskrng = Calendar.getInstance();
        waktuskrng.setTimeInMillis(System.currentTimeMillis());

        today = DateFormat.getDateInstance().format(new Date());

        /*if (!(waktuskrng.getTimeInMillis() > waktuakhirkerja.getTimeInMillis() || waktuskrng.getTimeInMillis() < waktumulaikerja.getTimeInMillis())) {*/
            if (preferenceManager.getsudahcheckin()) {
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
        /*}else {
            status = "notyettime";
            snotifikasi = ""
            notifikasi.setText(snotifikasi);
            ketnotifikasi.setText(sketnotifikasi);


        }*/

        Log.i("chek notif", ""+snotifikasi+" "+sketnotifikasi);
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

            getlocation();
            FirebaseDatabase.getInstance().getReference().child("DataPerusahaan").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    float latitudedestination = (float) dataSnapshot.child("latitude").getValue();
                    float longitudedestination = (float) dataSnapshot.child("longitude").getValue();
                    float results[] = new float[10];
                    Location.distanceBetween(latorigin, lngorigin, latitudedestination, longitudedestination, results);
                    distance = ""+results[0];

                    Toast.makeText(getActivity(), ""+distance, Toast.LENGTH_SHORT).show();



                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
            } else {
                Toast.makeText(getActivity(), "URI IS NULL", Toast.LENGTH_LONG).show();
            }

        }


    //CET LOCATION LATIDUTE LONGITUDE CURRENT LOCATION
    private void getlocation() {
        if (ActivityCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        } else {
            Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

            if (location != null){
                float latcurrent = (float) location.getLatitude();
                float lngcurrent = (float) location.getLongitude();
                latorigin = latcurrent;
                lngorigin = lngcurrent;

            }else {
                Toast.makeText(getActivity(),"Cant not get latitude longitude from current location",Toast.LENGTH_SHORT).show();
            }
        }
    }





    @Override
    public void onStart() {
        super.onStart();





    }

}
