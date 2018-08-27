package com.ebwebtech.rocket;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;

public class StartActivity extends AppCompatActivity implements View.OnClickListener{
   private Button mButton,mLoginButton;
   private EditText mEmail,mPassword;
   private ProgressDialog mProgress;
   private FirebaseAuth mAuth;
   private DatabaseReference mUsersDatabseReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        mButton = findViewById(R.id.register_button);
        mLoginButton = findViewById(R.id. login_loginbutton);
        mEmail = findViewById(R.id.login_email);
        mPassword = findViewById(R.id.login_password);
        mAuth = FirebaseAuth.getInstance();
        mProgress =new ProgressDialog(StartActivity.this);
        mUsersDatabseReference = FirebaseDatabase.getInstance().getReference().child("Users");
        mButton.setOnClickListener(this);
        mLoginButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.register_button)
        {
            Intent register = new Intent(StartActivity.this, RegisterActivity.class);
            startActivity(register);
        }
        if(v.getId() == R.id.login_loginbutton)
        {

             String email = mEmail.getText().toString().trim();
             String password = mPassword.getText().toString().trim();
             loginUser(email,password);
        }
    }

    private void loginUser(String email, String password)
    {
        mProgress.show();
        mProgress.setMessage("Authenticating");

        mProgress.setCancelable(false);
        mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                  if(task.isSuccessful()){
                      mProgress.dismiss();
                      String current_user_id = mAuth.getCurrentUser().getUid();
                      String deviceToken = FirebaseInstanceId.getInstance().getToken();

                      mUsersDatabseReference.child(current_user_id).child("device_token").setValue(deviceToken).addOnSuccessListener(new OnSuccessListener<Void>() {
                          @Override
                          public void onSuccess(Void aVoid) {
                              startActivity(new Intent(StartActivity.this,MainActivity.class));
                              finish();
                          }
                      });


                  }
                  else{
                        mProgress.dismiss();
                      Toast.makeText(StartActivity.this, "Could not login", Toast.LENGTH_SHORT).show();
                  }
            }
        });

    }
}
