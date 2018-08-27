package com.ebwebtech.rocket;


import android.content.Context;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import de.hdodenhof.circleimageview.CircleImageView;


/**
 * A simple {@link Fragment} subclass.
 */
public class FriendsFragment extends Fragment {
   private RecyclerView mRecyclerView;
   private DatabaseReference mFriendsDatabaseReference;
   private DatabaseReference mUsersDatabaseReference;
   private FirebaseUser mCurrentUser;
   private View view;

static Context mContext;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
         view = inflater.inflate(R.layout.fragment_friends, container, false);
        mRecyclerView =view.findViewById(R.id.friends_RecyclerView);
                     mCurrentUser = FirebaseAuth.getInstance().getCurrentUser();
        mFriendsDatabaseReference = FirebaseDatabase.getInstance().getReference().child("Friends").child(mCurrentUser.getUid());
        mUsersDatabaseReference = FirebaseDatabase.getInstance().getReference().child("Users");
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mContext = getActivity();
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseRecyclerAdapter<ModelFriends, FriendsViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<ModelFriends, FriendsViewHolder>(
                ModelFriends.class,
                R.layout.single_all_users_layout,
                FriendsViewHolder.class,
                mFriendsDatabaseReference

        ) {
            @Override
            protected void populateViewHolder(final FriendsViewHolder viewHolder, ModelFriends model, int position) {
                             viewHolder.setDate(model.getDate());

                             String list_users_id = getRef(position).getKey();
                             mUsersDatabaseReference.child(list_users_id).addValueEventListener(new ValueEventListener() {
                                 @Override
                                 public void onDataChange(DataSnapshot dataSnapshot) {
                                     String userName = dataSnapshot.child("name").getValue().toString();
                                     String userImage = dataSnapshot.child("thumb_image").getValue().toString();
                                     boolean userOnlineStatus = (boolean)dataSnapshot.child("online").getValue();
                                     Log.d("onlinestaturrrr", "onDataChange: "+ userOnlineStatus);
                                     viewHolder.setName(userName);
                                     viewHolder.setImage(userImage);
                                     viewHolder.setUserOnline(userOnlineStatus);
                                 }

                                 @Override
                                 public void onCancelled(DatabaseError databaseError) {

                                 }
                             });
            }
        };
        mRecyclerView.setAdapter(firebaseRecyclerAdapter);

    }
    public static class FriendsViewHolder extends RecyclerView.ViewHolder {
        View mView;
        public FriendsViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
        }
        public void setDate(String date)
        {
            TextView userNameView = mView.findViewById(R.id.single_user_status);
            userNameView.setText(date);
        }
        public void setName(String name)
        {
            TextView nameTextView= mView.findViewById(R.id.single_user_name);
            nameTextView.setText(name);
        }
        public void setImage(String image)
        {
            CircleImageView imageView = mView.findViewById(R.id.single_user_dp);
            Glide.with(mContext).load(image).into(imageView);
        }
        public void setUserOnline(boolean status)
        {
            ImageView onlineStatus = mView.findViewById(R.id.single_user_online);
              if(status==true)
              {
                  onlineStatus.setVisibility(View.VISIBLE);
              }
              else if(status == false)
              {
                  onlineStatus.setVisibility(View.GONE);
              }
        }
    }
}
