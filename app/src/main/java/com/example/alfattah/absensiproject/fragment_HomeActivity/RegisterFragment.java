package com.example.alfattah.absensiproject.fragment_HomeActivity;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.Toolbar;

import com.example.alfattah.absensiproject.MainActivity;
import com.example.alfattah.absensiproject.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;


/**
 * A simple {@link Fragment} subclass.
 */

public class RegisterFragment extends Fragment {


    public RegisterFragment() {
        // Required empty public constructor
    }

   // @BindView(R.id.reg_display_name)
    TextInputLayout displayname;
    //   @BindView(R.id.reg_jabatan)
    TextInputLayout jabatan;
    // @BindView(R.id.reg_email)
    TextInputLayout email;
    //  // @BindView(R.id.reg_password)
    TextInputLayout password;
    // @BindView(R.id.reg_no_hp)
    TextInputLayout noHp;
    // @BindView(R.id.reg_alamat)
    TextInputLayout alamat;
    // @BindView(R.id.reg_NIP)
    TextInputLayout NIP;
    // @BindView(R.id.reg_JenisLK)
    RadioGroup jenislk;
    // @BindView(R.id.laki)
    RadioButton laki;
    // @BindView(R.id.perempuan)
    RadioButton perempuan;

    private Button registrasi;

    @BindView(R.id.toolbarid)
    Toolbar mToolbar;
    private ProgressDialog mpProgressDialog;


    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    //database firebase
    private DatabaseReference mDatabaseReference;
    private DatabaseReference databaseUserCampur;
    private FirebaseUser currentUser;


    private ArrayAdapter<CharSequence> spinneradapter;
    private String sjenislk, sRole;

    private Context context;
    private Unbinder unbinder = null;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_register, container, false);
        //ButterKnife.bind(this,view);
        unbinder = ButterKnife.bind(getActivity());
        mAuth = FirebaseAuth.getInstance();
        context = getActivity();

        //inisialisasi
        displayname = view.findViewById(R.id.reg_display_name);
        jabatan = view.findViewById(R.id.reg_jabatan);
        email = view.findViewById(R.id.reg_email);
        password = view.findViewById(R.id.reg_password);
        noHp = view.findViewById(R.id.reg_no_hp);
        alamat = view.findViewById(R.id.reg_alamat);
        NIP = view.findViewById(R.id.reg_NIP);
        jenislk = view.findViewById(R.id.reg_JenisLK);
        laki = view.findViewById(R.id.laki);
        perempuan = view.findViewById(R.id.perempuan);


        Spinner spinner = view.findViewById(R.id.regRole);
        spinneradapter = ArrayAdapter.createFromResource(context, R.array.RoleUser, android.R.layout.simple_spinner_item);
        spinneradapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        spinner.setAdapter(spinneradapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                sRole = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        registrasi = view.findViewById(R.id.regist_btn);
        registrasi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                daftarOnclick();
            }
        });


        mpProgressDialog = new ProgressDialog(getActivity());

        return view;
    }

    //@OnClick(R.id.regist_btn)
    public void daftarOnclick() {

        String edisplayname = displayname.getEditText().getText().toString();
        Log.i("edisplayname", "" + edisplayname);
        String ejabatan = jabatan.getEditText().getText().toString();
        String eemail = email.getEditText().getText().toString();
        String epassword = password.getEditText().getText().toString();
        String eno_hp = noHp.getEditText().getText().toString();
        String ealamat = alamat.getEditText().getText().toString();
        String eNIP = NIP.getEditText().getText().toString();
        sjenislk = "";
        int jenisid = jenislk.getCheckedRadioButtonId();
        if (jenisid == laki.getId()) {
            sjenislk = laki.getText().toString();
        } else if (jenisid == perempuan.getId()) {
            sjenislk = perempuan.getText().toString();
        } else {
            sjenislk = "bukan apa2";
        }


        if (sRole.equalsIgnoreCase("Staff")) {
            sRole = "Staff";
        } else {
            sRole = "Monitoring";
        }


        if (!TextUtils.isEmpty(edisplayname) && !TextUtils.isEmpty(eemail) && !TextUtils.isEmpty(epassword)
                && !TextUtils.isEmpty(eno_hp) && !TextUtils.isEmpty(ealamat) && !TextUtils.isEmpty(sjenislk) && !TextUtils.isEmpty(ejabatan) && !TextUtils.isEmpty(eNIP) && !TextUtils.isEmpty(sRole)) {
            mpProgressDialog.setTitle("Creating new acount..");
            mpProgressDialog.setMessage("Please wait.. while we create your acount..");
            mpProgressDialog.setCanceledOnTouchOutside(false);
            mpProgressDialog.show();
            register_user(edisplayname, eemail, epassword, eno_hp, ealamat, sRole, sjenislk, ejabatan,eNIP);
        } else {
            mpProgressDialog.hide();
            Toast.makeText(context, "Harap isi seluruh form", Toast.LENGTH_SHORT).show();
        }

    }

    private void register_user(final String edisplayname, final String eemail, final String epassword, final String eno_hp, final String ealamat, final String sRole, final String sjenislk, final String sjabatan,final  String snip) {
        mAuth.createUserWithEmailAndPassword(eemail, epassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    currentUser = FirebaseAuth.getInstance().getCurrentUser();
                    String uid = currentUser.getUid();

                    //create tabel user
                    mDatabaseReference = FirebaseDatabase.getInstance().getReference().child("Users").child(uid);

                    HashMap<String, String> userMap = new HashMap<>();
                    userMap.put("nama", "" + edisplayname);
                    userMap.put("jabatan", "" + sjabatan);
                    userMap.put("email", "" + eemail);
                    userMap.put("password", "" + epassword);
                    userMap.put("noHp", "" + eno_hp);
                    userMap.put("alamat", "" + ealamat);
                    userMap.put("role", "" + sRole);
                    userMap.put("jenisKelamin", "" + sjenislk);
                    userMap.put("thumb_image", "default");
                    userMap.put("location", "default");
                    userMap.put("locationLatLng", "default");
                    userMap.put("checkIN", "false");
                    userMap.put("checkInTime", "default");
                    userMap.put("checkOutTime", "default");
                    userMap.put("image", "default");
                    userMap.put("nip", snip);

                    mDatabaseReference.setValue(userMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                mpProgressDialog.dismiss();
                                Intent intent = new Intent(context, MainActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            } else {
                                mpProgressDialog.hide();
                                Toast.makeText(context, "Terjadi Kesalahan !!, periksa jaringan anda dan coba lagi", Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                } else {
                    mpProgressDialog.hide();
                    Toast.makeText(context, "" + task.getException() + "Registrasi dengan FirebaseAuth failed ..", Toast.LENGTH_LONG).show();
                }

            }
        });
    }


}
