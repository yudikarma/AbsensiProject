package com.example.alfattah.absensiproject;

import android.app.ProgressDialog;
import android.content.Intent;
import android.location.LocationManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.alfattah.absensiproject.utils.PreferenceManager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class ChekoutActionActivity extends AppCompatActivity {
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
    private DatabaseReference mcheckoutReference;
    private DatabaseReference mchekinReference;
    private String uid,islogin ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chekout_action);
        notifikasi = findViewById(R.id.notifikasi_chekin);
        ketnotifikasi = findViewById(R.id.ket_notif);

        floatingActionButton = findViewById(R.id.fab_chekin);

        preferenceManager  = new PreferenceManager(this);
        mStorageReference = FirebaseStorage.getInstance().getReference();
        mCurrentuser = FirebaseAuth.getInstance().getCurrentUser();
        mProgressDialog = new ProgressDialog(ChekoutActionActivity.this);
        uid = mCurrentuser.getUid();
        mDatabaseReference = FirebaseDatabase.getInstance().getReference().child("Users").child(uid);
        mcheckoutReference = FirebaseDatabase.getInstance().getReference().child("checkout").child(uid);
        mchekinReference = FirebaseDatabase.getInstance().getReference();
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDatabaseReference.child("checkIN").setValue("false");
                final String currenttime = DateFormat.getTimeInstance().format(new Date());
                Map update = new HashMap();
                update.put("uid", ""+uid);
                update.put("time", ""+currenttime);
                update.put("timestamp", ServerValue.TIMESTAMP);

                mcheckoutReference.updateChildren(update).addOnCompleteListener(new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {

                        Map deletecheckin = new HashMap();
                        deletecheckin.put("Checkin/"+uid, null);
                        mchekinReference.updateChildren(deletecheckin, new DatabaseReference.CompletionListener() {
                            @Override
                            public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {

                                preferenceManager.setsudahChekin(false);
                                status = "notchecked";
                                snotifikasi = "Checkout berhasil, terima kasih";
                                sketnotifikasi =  "Sampai ketemu esok hari :)";
                                floatingActionButton.setVisibility(View.GONE);
                                notifikasi.setText(snotifikasi);
                                ketnotifikasi.setText(sketnotifikasi);
                                Toast.makeText(ChekoutActionActivity.this, "Checkout berhasil, Terima Kasih", Toast.LENGTH_LONG);
                                Intent intent = new Intent(ChekoutActionActivity.this,MainActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

                                startActivity(intent);
                                finish();
                            }
                        });



                    }
                });
            }
        });




    }
}
