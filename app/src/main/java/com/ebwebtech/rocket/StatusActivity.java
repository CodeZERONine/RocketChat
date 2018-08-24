package com.ebwebtech.rocket;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class StatusActivity extends AppCompatActivity {
  private Toolbar mToolbar;
  private EditText mStatus;
  private Button mupdate;
  private FirebaseUser mCurrentUser;
  private DatabaseReference mDatabaseReference;
  String statusIntent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_status);
        mToolbar = findViewById(R.id.mToolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Account Status");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

         statusIntent = getIntent().getStringExtra("statusExtra");
        mStatus = findViewById(R.id.status_editText);
        mStatus.setText(statusIntent);
        mupdate = findViewById(R.id.status_update_Button);

        mCurrentUser = FirebaseAuth.getInstance().getCurrentUser();
        mDatabaseReference = FirebaseDatabase.getInstance().getReference().child("Users").child(mCurrentUser.getUid());
        mupdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                  String status= mStatus.getText().toString().trim();
                    mDatabaseReference.child("status").setValue(status).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful())
                            {
                                finish();
                            }
                            else
                            {
                                Toast.makeText(StatusActivity.this, "Some error has occurred while updating status..", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

            }
        });
    }
}
