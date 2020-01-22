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

public class fragment3Activity extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        LinearLayout linearLayout = (LinearLayout) inflater.inflate(R.layout.fragment3, container, false);
        final String[] data = {"신효정","임세민","김지윤","조다솜","황하영","김혜유"};
        Button search_btn = linearLayout.findViewById(R.id.search_btn);
        final EditText editbox = linearLayout.findViewById(R.id.editbox);
        int nDatCnt=0;

        ArrayList<itemActivity> oData = new ArrayList<>();


        for (int i=0; i<5; ++i)
        {
            itemActivity oItem = new itemActivity();
            oItem.name = data[nDatCnt++];
            oData.add(oItem);
            if (nDatCnt >= data.length) nDatCnt = 0;
        }

        final ListView list3 = linearLayout.findViewById(R.id.list3);
        adapterActivity Adapter = new adapterActivity(oData);
        list3.setAdapter(Adapter);
        list3.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);

        search_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(editbox.getText().toString().equals("신효정")) {
                    list3.setItemChecked(0,true);
                }else if(editbox.getText().toString().equals("임세민")) {
                    list3.setItemChecked(1,true);
                }else if(editbox.getText().toString().equals("김지윤")) {
                    list3.setItemChecked(2,true);
                }else if(editbox.getText().toString().equals("조다솜")) {
                    list3.setItemChecked(3,true);
                }else if(editbox.getText().toString().equals("황하영")) {
                    list3.setItemChecked(4,true);
                }else if(editbox.getText().toString().equals("김혜유")) {
                    list3.setItemChecked(5,true);
                }
            }
        });

        return linearLayout;
    }
    }



