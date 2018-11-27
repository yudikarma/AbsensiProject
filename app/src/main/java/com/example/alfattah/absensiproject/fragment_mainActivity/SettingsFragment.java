package com.example.alfattah.absensiproject.fragment_mainActivity;


import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.esafirm.imagepicker.features.ImagePicker;
import com.esafirm.imagepicker.features.ReturnMode;
import com.esafirm.imagepicker.model.Image;
import com.example.alfattah.absensiproject.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import id.zelory.compressor.Compressor;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 */
public class SettingsFragment extends AppCompatActivity {


    public SettingsFragment() {
        // Required empty public constructor
    }

    private DatabaseReference mDatabaseReference;
    private FirebaseUser mCurrentuser;

    private static final int GALLERY_PICK = 1;

    //Android Layout
    private CircleImageView mCircleImageView;
    private TextView mDisplayname;
    private TextView maddresprofil;
    private TextView email,name,jabatan,nip,rule,nohp_setting,address_setting;
    String snama,semail,saddress,sjabatan,snip,srule,snohp,simage;
    private FloatingActionButton btn_changeimage_setting;
    private ImageView btn_changestatus_settinng;
    private ProgressDialog mProgressDialog;

    private Compressor compressedImageBitmap;

    //Firebase
    private StorageReference mStorageReference;
    static final int REQUEST_IMAGE_CAPTURE = 2;
    private File imageFile = null;
    //VARIABEL UNTUK TAKE PICTURE
    private Uri imageUri;
    private Uri newsImageUri;
    private File photofile = null;
    private File thumb_file;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_settings);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        mCircleImageView = findViewById(R.id.circleImageViewdetail);
        mDisplayname = findViewById(R.id.display_name_detail);
        maddresprofil = findViewById(R.id.addres_profil);
        email = findViewById(R.id.email_detail);
        name = findViewById(R.id.name_detail);
        jabatan = findViewById(R.id.jabatan_detail);
        nip  =  findViewById(R.id.NIP_detail);
        rule =  findViewById(R.id.Rule);
        nohp_setting =  findViewById(R.id.nohp_detail);
        address_setting = findViewById(R.id.address_detail);

        mProgressDialog = new ProgressDialog(SettingsFragment.this);
        mStorageReference = FirebaseStorage.getInstance().getReference();

        mCurrentuser = FirebaseAuth.getInstance().getCurrentUser();
        final String uid = mCurrentuser.getUid();
        mDatabaseReference = FirebaseDatabase.getInstance().getReference().child("Users").child(uid);

        mDatabaseReference.keepSynced(true);

        mDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

              /*  if (getActivity() == null){
                    return;
                }else {*/
                if (SettingsFragment.this != null){
                    snama = dataSnapshot.child("nama").getValue().toString();
                    saddress = dataSnapshot.child("alamat").getValue().toString();
                    semail = dataSnapshot.child("email").getValue().toString();
                    sjabatan = dataSnapshot.child("jabatan").getValue().toString();
                    snip = dataSnapshot.child("nip").getValue().toString();
                    srule = dataSnapshot.child("role").getValue().toString();
                    snohp = dataSnapshot.child("noHp").getValue().toString();
                    simage = dataSnapshot.child("image").getValue().toString();

                    mDisplayname.setText(""+snama);
                    name.setText(""+snama);
                    maddresprofil.setText(""+saddress);;
                    email.setText(""+semail);
                    jabatan.setText(""+sjabatan);
                    nip.setText(""+snip);
                    rule.setText(""+srule);;
                    nohp_setting.setText(""+snohp);
                    address_setting.setText(""+saddress);
                    //set display image
                    Log.i("uri image", ""+simage);
                    if (!simage.equals("default")){
                        Glide.with(SettingsFragment.this).load(simage).listener(new RequestListener<Drawable>() {
                            @Override
                            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                return false;
                            }

                            @Override
                            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                return false;
                            }
                        }).into(mCircleImageView);
                    }else {


                    }
                }
            }
            /*  }
             */
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        btn_changeimage_setting = findViewById(R.id.change_image_setting);
        btn_changeimage_setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                galleryIntent(REQUEST_IMAGE_CAPTURE);
                /*Intent galery_intent = new Intent();
                galery_intent.setType("image/*");
                galery_intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(galery_intent,"Select Image"),GALLERY_PICK);*/
             /*   if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE)
                        == PackageManager.PERMISSION_DENIED) {

                    //Meminta Akses Camera
                    ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_IMAGE_CAPTURE);

                }
                //CHEK Permission MENYIMPAN FILE (Storage)
                else if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        == PackageManager.PERMISSION_DENIED) {

                    //MEMINTA ACCES
                    ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_IMAGE_CAPTURE);
                } else {


                    *//* ==========Jika AKses diberikan, maka penggil Intent Camera =========*//*
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
                }*/
            }
        });


    }

    private void galleryIntent(int requestImageCapture) {
        ImagePicker.create(this)
                .returnMode(ReturnMode.ALL) // set whether pick and / or camera action should return immediate result or not.
                .folderMode(true) // folder mode (false by default)
                .toolbarFolderTitle("Folder") // folder selection title
                .toolbarImageTitle("Tap to select") // image selection title
                .toolbarArrowColor(Color.WHITE) // Toolbar 'up' arrow color
                .single() // single mode
                .showCamera(true) // show camera or not (true by default)
                .imageDirectory("Camera") // directory name for captured image  ("Camera" folder by default)
                .enableLog(false) // disabling log
                .start(requestImageCapture); // start image picker activity with request code
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.e("masuk result", "masuk result");
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK){
            Image image = ImagePicker.getFirstImageOrNull(data);
            imageFile = new File(image.getPath());
            imageUri = Uri.fromFile(imageFile);
            //jika image Uri tidak kosong
            if (imageUri != null) {

                //Kompress File asal kedalam File Baru dengan ukuran lebih ringan
                try {
                    File newsfile = new Compressor(SettingsFragment.this)
                            .compressToFile(imageFile);
                    imageUri = Uri.fromFile(newsfile);

                    CropImage.activity(imageUri)
                            .setAspectRatio(1, 1)
                            .start(this);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                //Mendapatkan Uri file baru setelah di kompress
            }
        }
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            Log.i("info", "Crop Image di panggil");
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Log.i("info","Crop Image result OK" );
                mProgressDialog.setTitle("Change Profil Images");
                mProgressDialog.setMessage("we wil try change your profil images");
                mProgressDialog.setCanceledOnTouchOutside(false);
                mProgressDialog.show();

                imageUri  = result.getUri();
                mCurrentuser = FirebaseAuth.getInstance().getCurrentUser();
                final String uid = mCurrentuser.getUid();

                final File thum_file_path = new File(imageUri.getPath());


                Bitmap thum_bitmap = null;
                try {
                    thum_bitmap = new Compressor(SettingsFragment.this)
                            .setMaxHeight(200)
                            .setMaxWidth(200)
                            .setQuality(75)
                            .compressToBitmap(thum_file_path);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                thum_bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                final byte[] thumb_byte = baos.toByteArray();

                final StorageReference filepath = mStorageReference.child("Profile_images").child(uid + "Profile_images.jpg");
                final StorageReference thumb_filepath = mStorageReference.child("Profile_images").child("thumbnails_Profile_images").child(uid + "thumbs_profil_images.jpg");
                filepath.putFile(imageUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                        if (task.isSuccessful()) {
                            //Toast.makeText(SettingActivity.this,"working",Toast.LENGTH_LONG).show();

                            filepath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {

                                    final  Uri downloadUri = uri;
                                    final String sdownloadUri = downloadUri.toString();
                                    //upload task thumb
                                    UploadTask uploadTask = thumb_filepath.putBytes(thumb_byte);
                                    uploadTask.addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> thumb_task) {

                                            if (thumb_task.isSuccessful()) {


                                                Map update_hasmap = new HashMap();
                                                update_hasmap.put("image",""+sdownloadUri);
                                                update_hasmap.put("thumb_image","default");


                                                mDatabaseReference.updateChildren(update_hasmap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull final Task<Void> task) {
                                                        if (task.isSuccessful()) {
                                                            mProgressDialog.dismiss();
                                                            Toast.makeText(SettingsFragment.this, "Succes Upload", Toast.LENGTH_LONG).show();


                                                        }
                                                        else {
                                                            mProgressDialog.dismiss();
                                                            Toast.makeText(SettingsFragment.this, "Upload Failed", Toast.LENGTH_LONG).show();
                                                        }
                                                    }
                                                });
                                            } else {
                                                Toast.makeText(SettingsFragment.this, "Error upload thumb", Toast.LENGTH_LONG).show();
                                                mProgressDialog.dismiss();
                                            }
                                        }
                                    });

                                }
                            }) ;

                        } else {
                            Toast.makeText(SettingsFragment.this, "Error upload", Toast.LENGTH_LONG).show();
                            mProgressDialog.dismiss();
                        }

                    }
                });
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {

                mProgressDialog.hide();
                Exception error = result.getError();
                Log.i("info", "Crop Image Result error"+error);
            }
            else {
                Log.i("info", "Crop image != Result OK");
            }
        }
    }
}
