package com.ebwebtech.rocket;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;


/**
 * A simple {@link Fragment} subclass.
 */
public class ChatsFragment extends Fragment {
   private RecyclerView mRecyclerView;
   private DatabaseReference mDatabaseReference,mMessageReference, mConvDatabase;
   private FirebaseUser mCurrentUser;
   String mCurrent_UserID;

    public ChatsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =inflater.inflate(R.layout.fragment_chats, container, false);
        mRecyclerView = view.findViewById(R.id.conversation_recyclerView);
        mDatabaseReference = FirebaseDatabase.getInstance().getReference();

        mCurrentUser = FirebaseAuth.getInstance().getCurrentUser();
        mCurrent_UserID = mCurrentUser.getUid();
        mMessageReference = mDatabaseReference.child("Messages").child(mCurrent_UserID);
        mConvDatabase =     mDatabaseReference.child("Chat");
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setHasFixedSize(true);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        Query query = mConvDatabase.orderByChild("timestamp");

    }
}
