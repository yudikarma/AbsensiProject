package com.example.alfattah.absensiproject.fragment_HomeActivity;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.alfattah.absensiproject.HomeActivity;
import com.example.alfattah.absensiproject.MainActivity;
import com.example.alfattah.absensiproject.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;


/**
 * A simple {@link Fragment} subclass.
 */

public class LoginFragment extends Fragment {




    public LoginFragment() {
        // Required empty public constructor
    }

   /* @BindView(R.id.tvRegister)
    LinearLayout tvRegister;
    @BindView(R.id.tvforgotpassword)
    LinearLayout tvforgotpassword;*/
   private EditText mEmailView;
    private EditText mPasswordView;
    private View mProgressView;
    private View mLoginFormView;
    private Button mEmailSignInButton;
    private ProgressDialog mpProgressDialog;


    //Firebase
    private FirebaseAuth mAuth;
    //firebase user database
    private DatabaseReference muserDatabaseReference;

    private  String email;
    private String password;
    private  String Username;





    private HomeActivity homeActivity;
     private LinearLayout regist,forgot;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_login, container, false);
       /* ButterKnife.bind(view);*/
        homeActivity = new HomeActivity();
        mEmailView =  view.findViewById(R.id.email);
        mPasswordView =  view.findViewById(R.id.password);
        mpProgressDialog = new ProgressDialog(getActivity());

        //getdata user
        muserDatabaseReference = FirebaseDatabase.getInstance().getReference().child("Users");

        //AUth user
        mAuth = FirebaseAuth.getInstance();
        mEmailSignInButton = view.findViewById(R.id.email_sign_in_button);
        mEmailSignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email = mEmailView.getText().toString();
                password = mPasswordView.getText().toString();
                if (!TextUtils.isEmpty(email) && !TextUtils.isEmpty(password)){
                    mpProgressDialog.setTitle("login..");
                    mpProgressDialog.setMessage("we are try connect your acount..");
                    mpProgressDialog.setCanceledOnTouchOutside(false);
                    mpProgressDialog.show();
                    /* ======== Call method Login with email && password ========*/


                    loginUserfirebase(email,password);

                }else {
                    mpProgressDialog.hide();
                    Toast.makeText(getActivity(),"please insert your email and password login",Toast.LENGTH_SHORT).show();
                }

            }


        });



        regist = view.findViewById(R.id.tvRegister);
        forgot = view.findViewById(R.id.tvforgotpassword);
        regist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), HomeActivity.class);
                intent.putExtra("position", "1");
                startActivity(intent);
            }
        });
        forgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), HomeActivity.class);
                intent.putExtra("position", "2");
                startActivity(intent);
            }
        });

        return  view;
    }

    //METHOD FOR LOGIN WiTH EMAIL && PASSWORD FIREBASE
    private void loginUserfirebase(final String email, final String password) {
        mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){

                    //Token User for send Notification to device nantik
                    final String device_token = FirebaseInstanceId.getInstance().getToken();
                    final String current_user_id = mAuth.getCurrentUser().getUid();

                    muserDatabaseReference.child(current_user_id).child("device_token").setValue(device_token).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            mpProgressDialog.dismiss();

                            Intent intent = new Intent(getActivity(),MainActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

                            startActivity(intent);
                            getActivity().finish();
                        }
                    });


                }else{
                    mpProgressDialog.hide();
                    Toast.makeText(getActivity(),"Maaf Proses LoginActivity gagal, silahkan periksa koneksi jaringan anda.. Terima Kasih",Toast.LENGTH_SHORT).show();
                }
            }
        });

    }



}
