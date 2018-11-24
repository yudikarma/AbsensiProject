package com.example.alfattah.absensiproject.FragmentMonitoringDashboard;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.alfattah.absensiproject.CheckinInformationActivity;
import com.example.alfattah.absensiproject.Model.ChekinModel;
import com.example.alfattah.absensiproject.Model.GetTimeAgo;
import com.example.alfattah.absensiproject.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.firebase.ui.auth.AuthUI.getApplicationContext;

/**
 * A simple {@link Fragment} subclass.
 */
public class ListUserCheckINFragment extends Fragment {


    public ListUserCheckINFragment() {
        // Required empty public constructor
    }

    private FirebaseDatabase chekindatabase;
    private FirebaseDatabase userdatabase;
    private FirebaseRecyclerAdapter<ChekinModel,UserviewHolder> adapter;
    private TextView Notifnull;
    private RecyclerView recyclerView;

    //timeago
    private static final int SECOND_MILLIS = 1000;
    private static final int MINUTE_MILLIS = 60 * SECOND_MILLIS;
    private static final int HOUR_MILLIS = 60 * MINUTE_MILLIS;
    private static final int DAY_MILLIS = 24 * HOUR_MILLIS;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_list_user_check_in, container, false);
        recyclerView = rootView.findViewById(R.id.listcheckin);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setHasFixedSize(true);

        Query query = FirebaseDatabase.getInstance().getReference().child("Checkin").limitToLast(50);
        FirebaseRecyclerOptions options = new FirebaseRecyclerOptions.Builder<ChekinModel>()
                .setQuery(query, ChekinModel.class)
                .setLifecycleOwner(getActivity())
                .build();
        adapter = new FirebaseRecyclerAdapter<ChekinModel, UserviewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull final UserviewHolder holder, int position, @NonNull ChekinModel model) {

                holder.settime(model.getTime());
                holder.settimestamp(model.getDistance());
                final String uid = model.getUid();
                FirebaseDatabase.getInstance().getReference().child("Users").child(uid).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        holder.setNama(dataSnapshot.child("nama").getValue().toString());
                        holder.setMcCircleImageView(dataSnapshot.child("image").getValue().toString());

                        holder.mView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                Intent intent = new Intent(getActivity(), CheckinInformationActivity.class);
                                intent.putExtra("uid", uid);
                                startActivity(intent);

                            }
                        });
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

            }

            @NonNull
            @Override
            public UserviewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
             View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_singgle_layout, parent,false);
             return new UserviewHolder(view);
            }
        };

        recyclerView.setAdapter(adapter);


        return rootView;
    }

    public class UserviewHolder extends RecyclerView.ViewHolder {
        View mView;
        TextView mdisplayname,time,timestamp ;
        CircleImageView mcCircleImageView;
        public UserviewHolder(View itemView) {
            super(itemView);
            mView = itemView;

            mdisplayname = (TextView) mView.findViewById(R.id.user_singgle_name);
            time  = (TextView) mView.findViewById(R.id.user_single_time);
            timestamp = mView.findViewById(R.id.range);
            mcCircleImageView = (CircleImageView) mView.findViewById(R.id.profil_single_layout);



        }
        public void setNama(String display_name){

            mdisplayname.setText(display_name);

        }
        public void settime(String time1){
            time.setText(time1);
        }
        public void settimestamp(String jabatan){

            timestamp.setText(""+jabatan+" Meter");

        }
        public  void setMcCircleImageView(final String img_uri){
            //Picasso.with(UsersActivity.this).load(img_uri).placeholder(R.drawable.user).into(mcCircleImageView);
            if (!img_uri.equals("default") && img_uri!= null){
                Glide.with(getActivity()).load(img_uri).listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        return false;
                    }
                }).into(mcCircleImageView);
            }else {

            }
        }
    }




    @Override
    public void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();
    }
}
