package com.example.alfattah.absensiproject.Fragment_Dashboard_Staff;


import android.app.ProgressDialog;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.alfattah.absensiproject.R;
import com.example.alfattah.absensiproject.utils.PreferenceManager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * A simple {@link Fragment} subclass.
 */
public class CheckOutFragment extends Fragment {


    public CheckOutFragment() {
        // Required empty public constructor
    }

    private TextView notifikasi,ketnotifikasi;

    private PreferenceManager preferenceManager;
    private Calendar waktuskrng, waktumulaikerja   ,waktuakhirkerja ;
    String today, status,snotifikasi,sketnotifikasi;
    private FloatingActionButton floatingActionButton;

    //get lat lng from current location
    static final int REQUEST_LOCATION = 1;
    LocationManager locationManager;

    private StorageReference mStorageReference;
    private FirebaseUser mCurrentuser;
    private ProgressDialog mProgressDialog;
    private DatabaseReference mDatabaseReference;
    private DatabaseReference mcheckinReference;
    private String uid ;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_check_out, container, false);
        notifikasi = rootView.findViewById(R.id.notifikasi_chekin);
        ketnotifikasi = rootView.findViewById(R.id.ket_notif);

        floatingActionButton = rootView.findViewById(R.id.fab_chekin);

        preferenceManager  = new PreferenceManager(getActivity());
        mStorageReference = FirebaseStorage.getInstance().getReference();
        mCurrentuser = FirebaseAuth.getInstance().getCurrentUser();
        mProgressDialog = new ProgressDialog(getActivity());
        uid = mCurrentuser.getUid();
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

        if (!preferenceManager.getsudahcheckin()) {
            //true
            status = "notchecked";
            snotifikasi = "You haven't checked yet";
            sketnotifikasi =  "please go to the checkin page to check";
            floatingActionButton.setVisibility(View.GONE);
            notifikasi.setText(snotifikasi);
            ketnotifikasi.setText(sketnotifikasi);
        } else {

            if (waktuskrng.getTimeInMillis() < waktumulaikerja.getTimeInMillis() || waktuskrng.getTimeInMillis() > waktuakhirkerja.getTimeInMillis()) {
                //bisa check in
                status = "checkout";
                snotifikasi = "You haven't checked out";
                sketnotifikasi = "You haven't checked yet, please click the button below to Check out";
                floatingActionButton.setVisibility(View.VISIBLE);
                notifikasi.setText(snotifikasi);
                ketnotifikasi.setText(sketnotifikasi);


            } else {
                //gak bisa chekin
                status = "cantcheckout";
                snotifikasi = "Can't Chek Out";
                sketnotifikasi = "cannot Check Out at this time because it is not yet time";
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

        Log.i("chek notif", "" + snotifikasi + " " + sketnotifikasi);
        notifikasi.setText(snotifikasi);
        ketnotifikasi.setText(sketnotifikasi);


        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doCheckout();
            }
        });



        return rootView;
    }

    private void doCheckout() {
        if (status.equalsIgnoreCase("checkout")){
            preferenceManager.setsudahChekin(false);

        }
    }


}
