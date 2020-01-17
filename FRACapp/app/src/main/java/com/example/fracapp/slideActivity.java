package com.example.fracapp;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

public class slideActivity  extends FragmentPagerAdapter{

    int MAX_PAGE=2;
    Fragment cur_fragment=new Fragment();

    public slideActivity (FragmentManager fm) {
            super(fm);
        }
        @Override
        public Fragment getItem(int position) {
            if(position<0 || MAX_PAGE<=position)
                return null;
            switch (position){
                case 0:
                    cur_fragment=new noticeActivity();
                    break;
                case 1:
                    cur_fragment=new todolistActivity();
                    break;
            }
            return cur_fragment;
        }
        @Override
        public int getCount() {
            return MAX_PAGE;
        }
    }


