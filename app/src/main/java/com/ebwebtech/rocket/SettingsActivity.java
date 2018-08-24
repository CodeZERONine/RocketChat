package com.ebwebtech.rocket;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
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
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.Random;

import de.hdodenhof.circleimageview.CircleImageView;

public class SettingsActivity extends AppCompatActivity implements View.OnClickListener{
    private TextView mName, mStatus;
    private Button mChangeImage, mChangeStatus;
    private CircleImageView mProfileImage;

    private FirebaseUser mCurrentUser;
    private DatabaseReference mUserDatabase;
    private StorageReference mStorageRef;

    private static final int GALLERY_REQUEST=1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        mName = findViewById(R.id.setting_name);
        mStatus = findViewById(R.id.setting_status);
        mProfileImage = findViewById(R.id.setting_dp);
        mChangeImage = findViewById(R.id.setting_changeImage);

        mChangeStatus = findViewById(R.id.setting_changeStatus);
       mChangeStatus.setOnClickListener(this);
       mChangeImage.setOnClickListener(this);

        mCurrentUser = FirebaseAuth.getInstance().getCurrentUser();
        String mCurrentUID = mCurrentUser.getUid();
        mUserDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(mCurrentUID);
        mStorageRef = FirebaseStorage.getInstance().getReference();

        mUserDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                   String name = dataSnapshot.child("name").getValue().toString();
                String image = dataSnapshot.child("image").getValue().toString();
                String status = dataSnapshot.child("status").getValue().toString();
                String thumb_image = dataSnapshot.child("thumb_image").getValue().toString();
                mName.setText(name);
                mStatus.setText(status);
                Glide.with(SettingsActivity.this).load(image).into(mProfileImage);

                Log.d("uspa", "onDataChange: "+dataSnapshot.toString());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d("uspa", "onDataError: "+databaseError.toString());
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch(v.getId())
        {
            case R.id.setting_changeImage://sending an intent to gallery
                                                               Intent galleryIntent= new Intent();
                                                               galleryIntent.setType("image/*");
                                                               galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
                                                               startActivityForResult(Intent.createChooser(galleryIntent,"Select image:"),GALLERY_REQUEST);
                                                              //CropImage.activity().setGuidelines(CropImageView.Guidelines.ON).start(SettingsActivity.this);
                                                               break;
            case R.id.setting_changeStatus:
                Intent ji= new Intent(SettingsActivity.this,StatusActivity.class);
                                ji.putExtra("statusExtra",mStatus.getText().toString().trim());
                                  startActivity(ji);
                                   break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
          if(requestCode == GALLERY_REQUEST && resultCode == RESULT_OK)
          {
              Toast.makeText(this, "Inside acticivivResult", Toast.LENGTH_SHORT).show();
              Uri imageUri = data.getData();
               CropImage.activity(imageUri).setAspectRatio(1,1).start(SettingsActivity.this);
          }

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {

                Uri resultUri = result.getUri();
                //Now Store this URI to firebase storage
                   StorageReference filepath = mStorageRef.child("ProfilePictures").child(mCurrentUser.getUid()+".jpg");
                   filepath.putFile(resultUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                       @Override
                       public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                                   if(task.isSuccessful())
                                   {
                                       Toast.makeText(SettingsActivity.this, "Done", Toast.LENGTH_SHORT).show();
                                       String downloadURL = task.getResult().getDownloadUrl().toString();
                                       String downloadUrl = task.getResult().getDownloadUrl().toString();
                                       //Setting download link to databasem

                                       mUserDatabase.child("image").setValue(downloadURL).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                               @Override
                                                                         public void onComplete(@NonNull Task<Void> task) {
                                                                                           if(task.isSuccessful())
                                                                                                      {
                                                                                                      Toast.makeText(SettingsActivity.this, "Image Stored in data Base", Toast.LENGTH_SHORT).show();
                                                                                                         }
                                                               }
                                       });

                                   }
                                   else
                                   {
                                       Toast.makeText(SettingsActivity.this, "Error", Toast.LENGTH_SHORT).show();
                                   }
                        }
                   });

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }

    }
    public static String random() {
        Random generator = new Random();
        StringBuilder randomStringBuilder = new StringBuilder();
        int randomLength = generator.nextInt(20);
        char tempChar;
        for (int i = 0; i < randomLength; i++){
            tempChar = (char) (generator.nextInt(96) + 32);
            randomStringBuilder.append(tempChar);
        }
        return randomStringBuilder.toString();
    }
}
