package com.cookandroid.attendandroidapp;

import android.app.Notification;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.textclassifier.TextClassification;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.CollationElementIterator;
import java.util.ArrayList;

public class fragment1Activity extends Fragment {
  
    public fragment1Activity() {
    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
   
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        LinearLayout linearLayout = (LinearLayout) inflater.inflate(R.layout.fragment1, container, false);
        Button search_btn = linearLayout.findViewById(R.id.check_btn);
        final TextView text = linearLayout.findViewById(R.id.text);
        int nDatCnt = 0;
        ArrayList<itemActivity> oData = new ArrayList<>();
        final String[] data = {"000001", "000002", "000003", "000004", "000005", "000006"};


        for (int i = 0; i < data.length; ++i) {
            itemActivity oItem = new itemActivity();
            oItem.data = data[nDatCnt++];
            oData.add(oItem);
            if (nDatCnt >= data.length) nDatCnt = 0;
        }


        final ListView list1 = linearLayout.findViewById(R.id.list1);
        adapterActivity Adapter = new adapterActivity(oData);
        list1.setAdapter(Adapter);
        list1.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);

        search_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (text.getText().toString()) {

                    case "000001":
                        list1.setItemChecked(0, true);
                        break;
                    case "000002":
                        list1.setItemChecked(1, true);
                        break;
                    case "000003":
                        list1.setItemChecked(2, true);
                        break;
                    case "000004":
                        list1.setItemChecked(3, true);
                        break;
                    case "000005":
                        list1.setItemChecked(4, true);
                        break;
                    case "000006":
                        list1.setItemChecked(5, true);
                        break;

                }
            }
        });

        return linearLayout;
    }

    public static class NetworkTask extends AsyncTask<Void, Void, String> {

        private String url, fileName;
        private CollationElementIterator text;


        public NetworkTask(String url, String fileName) {
            this.url = url;
            this.fileName = fileName;
        }

        @Override
        protected String doInBackground(Void... params) {
            String result; // 요청 결과를 저장할 변수.
            result = attendActivity.HttpURLConnection(url, "", fileName);
            return result;

        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            //doInBackground()로 부터 리턴된 값이 onPostExecute()의 매개변수로 넘어오므로 s를 출력한다.
            Toast.makeText(getApplicationContext(),"response:"+s,Toast.LENGTH_SHORT);
            text.setText(s);
        }

        private Context getApplicationContext() {
            return getApplicationContext();
        }
    }

    }



