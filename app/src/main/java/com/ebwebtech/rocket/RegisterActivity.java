package com.ebwebtech.rocket;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {
   private TextInputLayout mDNameL, mEmailL, mPasswordL;
    private TextInputEditText  mDNameE, mEmailE, mPasswordE;
    private Button mRegisterButton;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        mDNameE = findViewById(R.id.register_dName_TIE);
        mEmailE = findViewById(R.id.register_email_TIE);
        mPasswordE = findViewById(R.id.register_password_TIE);

        mAuth = FirebaseAuth.getInstance();

        mRegisterButton = findViewById(R.id.button2);

        mRegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String displayName = mDNameE.getText().toString().toLowerCase().trim();
                String email = mEmailE.getText().toString().toLowerCase().trim();
                String password = mPasswordE.getText().toString().toLowerCase().trim();
                registerUser(email,password);
            }
        });
    }

    private void registerUser(String email, String password) {
                mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                          if(task.isSuccessful())
                          {
                              FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
                              String UID = currentUser.getUid();
                              mDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(UID);

                             /* mDatabase.child("Users").child(UID);
                             both are correct
                              mDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(UID);*/
                             String token = FirebaseInstanceId.getInstance().getToken();
                             HashMap<String, String> userData = new HashMap<>();
                              userData.put("device_token",token);
                              userData.put("name",mDNameE.getText().toString().trim());
                              userData.put("status","Hey there! I am using Rocket");
                              userData.put("image","default");
                              userData.put("thumb_image","default");

                              mDatabase.setValue(userData).addOnCompleteListener(new OnCompleteListener<Void>() {
                                  @Override
                                  public void onComplete(@NonNull Task<Void> task) {
                                      if(task.isSuccessful())
                                      {
                                          Intent m = new Intent(RegisterActivity.this,MainActivity.class);
                                          startActivity(m);
                                          finish();
                                      }
                                  }
                              });


                          }
                          else
                              {
                                  Log.d("oppo", "onComplete: else case" +task.toString());
                                  Toast.makeText(RegisterActivity.this, "Error While registering new User"+"task.toString()", Toast.LENGTH_LONG).show();
                          }
                    }
                });
    }
}
