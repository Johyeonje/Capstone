package com.cookandroid.attendandroidapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;

public class fragment1Activity extends Fragment {

    public fragment1Activity() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        LinearLayout linearLayout = (LinearLayout) inflater.inflate(R.layout.fragment1, container, false);
        final String[] name = {"신효정","임세민","김지윤","조다솜","황하영","김혜유"};
        final String[] number = {"201720550","201623570","201724610","201621460","201924370","201721692"};
        Button search_btn = linearLayout.findViewById(R.id.search_btn);
        final EditText editbox = linearLayout.findViewById(R.id.editbox);
        int nDatCnt=0;

        ArrayList<itemActivity> oData = new ArrayList<>();


        for (int i=0; i<5; ++i)
        {
            itemActivity oItem = new itemActivity();
            oItem.name = name[nDatCnt++];
            oData.add(oItem);
            if (nDatCnt >= name.length) nDatCnt = 0;
        }


        final ListView list1 = linearLayout.findViewById(R.id.list1);
        adapterActivity Adapter = new adapterActivity(oData);
        list1.setAdapter(Adapter);
        list1.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);

        search_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               if(editbox.getText().toString().equals("신효정")) {
                    list1.setItemChecked(0,true);
               }else if(editbox.getText().toString().equals("임세민")) {
                   list1.setItemChecked(1,true);
               }else if(editbox.getText().toString().equals("김지윤")) {
                   list1.setItemChecked(2,true);
               }else if(editbox.getText().toString().equals("조다솜")) {
                   list1.setItemChecked(3,true);
               }else if(editbox.getText().toString().equals("황하영")) {
                   list1.setItemChecked(4,true);
               }else if(editbox.getText().toString().equals("김혜유")) {
                   list1.setItemChecked(5,true);
               }
            }
            });

     return linearLayout;
    }
}

