package com.example.send;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class fragment extends Fragment {

    ViewGroup viewGroup;
    TextView text2;

    public fragment() {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

            viewGroup = (ViewGroup) inflater.inflate(R.layout.fragment, container, false);
            text2 = viewGroup.findViewById(R.id.text2);
            Bundle bundle = getArguments();
            String texts = bundle.getString("texts");
            text2.setText(texts);

            return viewGroup;
        }
    }