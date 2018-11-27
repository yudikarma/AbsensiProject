package com.example.alfattah.absensiproject.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
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
import com.example.alfattah.absensiproject.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class CheckinDiffutilsAdapter extends RecyclerView.Adapter<CheckinDiffutilsAdapter.CheckinviewHolder> {

    private List<ChekinModel> chekinModels = new ArrayList<>();
    Context context;

    public CheckinDiffutilsAdapter(Context context, List<ChekinModel> chekinModels) {
        this.context = context;
        this.chekinModels = chekinModels;
    }

    @NonNull
    @Override
    public CheckinviewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_singgle_layout, parent,false);
        return new CheckinviewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final CheckinviewHolder holder, int position) {
        final ChekinModel getchekinModel = new ChekinModel();
        holder.settime(getchekinModel.getTime());
        holder.settimestamp(getchekinModel.getDistance());
        final String uid = getchekinModel.getUid();
        FirebaseDatabase.getInstance().getReference().child("Users").child(uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                holder.setNama(dataSnapshot.child("nama").getValue().toString());
                holder.setMcCircleImageView(""+dataSnapshot.child("image").getValue().toString());

                holder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                       /* Intent intent = new Intent(getActivity(), CheckinInformationActivity.class);
                        intent.putExtra("uid", uid);
                        startActivity(intent);*/
                       Intent intent = new Intent(context,CheckinInformationActivity.class);
                       intent.putExtra("uid", uid);
                       context.startActivity(intent);


                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    @Override
    public int getItemCount() {
        return chekinModels.size();
    }

    public class CheckinviewHolder extends RecyclerView.ViewHolder {
        View mView;
        TextView mdisplayname,time,timestamp ;
        CircleImageView mcCircleImageView;
        public CheckinviewHolder(View itemView) {
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
                Glide.with(context).load(img_uri).listener(new RequestListener<Drawable>() {
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
}
