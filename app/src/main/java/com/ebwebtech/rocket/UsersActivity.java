package com.ebwebtech.rocket;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import de.hdodenhof.circleimageview.CircleImageView;

public class UsersActivity extends AppCompatActivity {
   private RecyclerView mRecyclerView;
   private Toolbar mToolbar;
   private DatabaseReference mReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users);
        mToolbar = findViewById(R.id.users_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("All Users");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mRecyclerView = findViewById(R.id.users_RecyclerView);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mReference = FirebaseDatabase.getInstance().getReference().child("Users");
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseRecyclerAdapter<ModelAllUsers, UsersViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<ModelAllUsers, UsersViewHolder>
                (
                        ModelAllUsers.class,
                        R.layout.single_all_users_layout,
                        UsersViewHolder.class,
                        mReference
                ) {
            @Override
            protected void populateViewHolder(UsersViewHolder viewHolder, ModelAllUsers model, int position) {
                      viewHolder.setName(model.getName());
                      viewHolder.setImage(model.getThumb_image(),UsersActivity.this);
                      viewHolder.setStatus(model.getStatus());

                      final String userd_id = getRef(position).getKey();

                      viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                          @Override
                          public void onClick(View v) {
                                    Intent profileIntent = new Intent(UsersActivity.this,ProfileActivity.class);
                                    profileIntent.putExtra("user_id",userd_id);
                                    startActivity(profileIntent);
                          }
                      });
            }
        };
        firebaseRecyclerAdapter.startListening();
          mRecyclerView.setAdapter(firebaseRecyclerAdapter);
    }

    public static class UsersViewHolder extends RecyclerView.ViewHolder {
        View mView;
        public UsersViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
        }
        public void setName(String name)
        {
            TextView userNameView = mView.findViewById(R.id.single_user_name);
            userNameView.setText(name);
        }
        public void setImage( String image,Context mContext){
            CircleImageView userImageView = mView.findViewById(R.id.single_user_dp);
            Glide.with(mContext).load(image).into(userImageView);
        }
        public void setStatus(String status)
        {
            TextView userStatusView = mView.findViewById(R.id.single_user_status);
            userStatusView.setText(status);
        }
    }
}
