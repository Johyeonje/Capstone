package com.cookandroid.attendandroidapp;

import android.content.Context;
import android.os.Bundle;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

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
        final String[] data = {"a", "b", "c", "d", "e", "f"};
        Button search_btn = linearLayout.findViewById(R.id.search_btn);
        final EditText editbox = linearLayout.findViewById(R.id.editbox);

        final ListView list1 = linearLayout.findViewById(R.id.list1);
        list1.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_multiple_choice, data);
        list1.setAdapter(adapter);

        search_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch(editbox) {

                }

     return linearLayout;
    }
}

