package com.example.alfattah.absensiproject;

import android.annotation.SuppressLint;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.alfattah.absensiproject.map_Helper.GetTimeAgo;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.firebase.ui.auth.AuthUI.getApplicationContext;

public class CheckinInformationActivity extends AppCompatActivity {
private String uid;
private CircleImageView circleImageView;
private TextView displayname,latitude,longitude,time,distance,timeago;
private ImageView selfiecheckin;
private Button finish;
private Toolbar mToolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkin_information);
        uid = getIntent().getStringExtra("uid");
        circleImageView = findViewById(R.id.profil_image);
        selfiecheckin = findViewById(R.id.checkin_selfie);
        displayname = findViewById(R.id.checkin_displayname);
        latitude = findViewById(R.id.checkin_latitude);
        longitude = findViewById(R.id.checkin_longitude);
        time = findViewById(R.id.checkin_time);
        timeago = findViewById(R.id.checkin_timestamp);

        finish = findViewById(R.id.checkin_btn_back);
        finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mToolbar = findViewById(R.id.profil_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               /* Intent gotomain = new Intent(ProfilActivity.this,UsersActivity.class);
                gotomain.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(gotomain);*/
                finish();
            }
        });


        FirebaseDatabase.getInstance().getReference().child("Users").child(uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                setprofil(dataSnapshot.child("image").getValue().toString());

                displayname.setText(dataSnapshot.child("nama").getValue().toString());

                FirebaseDatabase.getInstance().getReference().child("Checkin").child(uid).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        setSelfiecheckin(dataSnapshot.child("image").getValue().toString());
                        latitude.setText(dataSnapshot.child("latitude").getValue().toString());
                        longitude.setText(dataSnapshot.child("longitude").getValue().toString());
                        time.setText(dataSnapshot.child("time").getValue().toString());
                        long timestamp = (long) dataSnapshot.child("timestamp").getValue();
                        GetTimeAgo getTimeAgo = new GetTimeAgo();


                        @SuppressLint("RestrictedApi") String lastSeenTime = getTimeAgo.getTimeAgo(timestamp, getApplicationContext());

                        timeago.setText(lastSeenTime);


                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void setprofil(String imageuri) {
        if (!imageuri.equals("default")){
            Glide.with(CheckinInformationActivity.this).load(imageuri).listener(new RequestListener<Drawable>() {
                @Override
                public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                    return false;
                }

                @Override
                public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                    return false;
                }
            }).into(circleImageView);
        }else {

        }
    }
    private void setSelfiecheckin(String imageuri) {
        if (!imageuri.equals("default")){
            Glide.with(CheckinInformationActivity.this).load(imageuri).listener(new RequestListener<Drawable>() {
                @Override
                public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                    return false;
                }

                @Override
                public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                    return false;
                }
            }).into(selfiecheckin);
        }else {

        }
    }
}
