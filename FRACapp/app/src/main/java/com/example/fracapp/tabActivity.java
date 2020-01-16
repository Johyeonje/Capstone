package com.example.fracapp;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.ArrayList;

public class tabActivity extends FragmentPagerAdapter {


    private String[] VIEW_MAPNTOP_TITLES = {"TAB1", "TAB2", "TAB3"};
    private ArrayList<Fragment> fList;


        public tabActivity(FragmentManager fm, ArrayList<Fragment> fList) {
            super(fm);
            this.fList = fList;
        }

        @Override
        public CharSequence getPageTitle(int position) {

            return VIEW_MAPNTOP_TITLES[position];

        }

        @Override
        public Fragment getItem(int position) {
            return this.fList.get(position);
        }

        @Override
        public int getCount() {
            return fList.size();
        }
    }

