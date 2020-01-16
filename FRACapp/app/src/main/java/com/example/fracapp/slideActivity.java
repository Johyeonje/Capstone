package com.example.fracapp;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.ArrayList;

public class slideActivity extends  FragmentPagerAdapter {


    private ArrayList<Fragment> sList;


        public slideActivity(FragmentManager sm, ArrayList<Fragment> sList) {
            super(sm);
            this.sList = sList;
        }

        @Override
        public Fragment getItem(int position) {
            return this.sList.get(position);
        }

        @Override
        public int getCount() {
            return sList.size();
        }
    }
