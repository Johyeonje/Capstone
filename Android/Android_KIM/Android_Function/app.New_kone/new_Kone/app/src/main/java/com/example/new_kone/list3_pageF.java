package com.example.new_kone;

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

public class list3_pageF extends Fragment {

    public list3_pageF() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        LinearLayout linearLayout = (LinearLayout) inflater.inflate(R.layout.list3, container, false);
        final String[] data = {"blue   201720550","black   201623570","pink   201724610","green   201621460","red   201924370","yellow   201721692"};
        Button search_btn = linearLayout.findViewById(R.id.check_btn);
        final EditText editbox = linearLayout.findViewById(R.id.editbox);
        int nDatCnt=0;

        ArrayList<list_item> oData = new ArrayList<>();


        for (int i=0; i<data.length; ++i)
        {
            list_item oItem = new list_item();
            oItem.data = data[nDatCnt++];
            oData.add(oItem);
            if (nDatCnt >= data.length) nDatCnt = 0;
        }


        final ListView list3 = linearLayout.findViewById(R.id.list3);
        list_adapter Adapter = new list_adapter(oData);
        list3.setAdapter(Adapter);
        list3.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);

        search_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch(editbox.getText().toString()) {

                    case "blue":
                        list3.setItemChecked(0,true);
                        break;
                    case "black":
                        list3.setItemChecked(1,true);
                        break;
                    case "pink":
                        list3.setItemChecked(2,true);
                        break;
                    case "green":
                        list3.setItemChecked(3,true);
                        break;
                    case "red":
                        list3.setItemChecked(4,true);
                        break;
                    case "yellow":
                        list3.setItemChecked(5,true);
                        break;

                }
            }
        });

        return linearLayout;
    }
}
