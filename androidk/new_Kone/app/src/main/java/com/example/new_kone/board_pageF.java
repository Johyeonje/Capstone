package com.example.new_kone;

//import android.support.annotation.Nullable;
//import android.support.v4.app.Fragment;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class board_pageF extends Fragment {

    public static androidx.fragment.app.Fragment newInstance() {
        board_pageF fragment = new board_pageF();


        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.board_page,container,false);
    }
}
