package com.ebwebtech.rocket;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity
{
   private FirebaseAuth mAuth;
   private Toolbar mToolbar;
   private ViewPager mPager;
   private SectionsPagerAdapter mSectionsPagerAdapter;
   private TabLayout mTabs;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mToolbar = findViewById(R.id.mainToolbar);
        setSupportActionBar(mToolbar);
        mToolbar.setTitle("Rocket");
        mAuth = FirebaseAuth.getInstance();
        mPager = findViewById(R.id.tabPager);
        mSectionsPagerAdapter= new SectionsPagerAdapter(getSupportFragmentManager());
        mPager.setAdapter(mSectionsPagerAdapter);
        mTabs = findViewById(R.id.main_tabs);
        mTabs.setupWithViewPager(mPager );
        }

    @Override
    protected void onStart() {
        super.onStart();
        //getting the current logged in user and saving that refernce into currentUser of type FirebaseUser
        FirebaseUser currentUser = mAuth.getCurrentUser();
         if(currentUser == null)
         {
             //no user has logged in
             sendToStart();
         }
    }

    private void sendToStart() {
        Intent i = new Intent(MainActivity.this, StartActivity.class);
        startActivity(i);
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_options_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId())
        {
            case R.id.action_logout: mAuth.getInstance().signOut();
                                                    sendToStart();
                                                    break;
            case R.id.action_account_setting:
                                                      startActivity(new Intent(MainActivity.this,SettingsActivity.class));
                                                     break;
            case R.id.action_all_users: startActivity(new Intent(MainActivity.this,UsersActivity.class));
                                                      break;
        }
        return true;
    }
}
