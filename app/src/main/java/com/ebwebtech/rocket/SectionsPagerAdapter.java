package com.ebwebtech.rocket;


import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

class SectionsPagerAdapter extends FragmentPagerAdapter{

    public SectionsPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch(position)
        {
            case 0: RequestsFragment fragment = new RequestsFragment();
                        return fragment;
            case 1: ChatsFragment fragment1 = new ChatsFragment();
                return fragment1;
            case 2: FriendsFragment fragment2 = new FriendsFragment();
                return fragment2;
        }
        return null;
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        switch(position)
        {
            case 0: return "Requests";
            case 1: return "Chats";
            case 2: return "Friends";
        }
        return super.getPageTitle(position);
    }
}
