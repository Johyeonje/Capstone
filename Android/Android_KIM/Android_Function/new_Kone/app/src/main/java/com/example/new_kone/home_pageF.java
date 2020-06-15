package com.example.new_kone;

//import android.app.Fragment;
//import android.support.v4.app.Fragment;
//import android.support.annotation.Nullable;
import android.content.Context;
import android.net.Uri;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.Toast;


public class home_pageF extends Fragment {


    private Context context;
    private WebView mWebView;
    private WebSettings mWebSettings;
    String Session_key;

    private static final String mKEY = "Session_k"; // 다음 스트링을 변수로 일단 임시로 넣는다.

    public static androidx.fragment.app.Fragment newInstance(String session_k) {
        home_pageF fragment = new home_pageF();
        Bundle mSession = new Bundle();
        mSession.putString(mKEY, session_k);
        fragment.setArguments(mSession);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) { // 다음 함수가 생성될때 실행하는 파트
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) { // 다음 화면이 띄워지고 실행하는 부분 Activity에서 oncreate부분에 사용하는 것들을 이 곳에 넣으면 된다.

        context = container.getContext();
        View view = inflater.inflate(R.layout.home_page,container,false);
      Button btn1 = (Button)view.findViewById(R.id.take_photo);
      Button btn2 = (Button)view.findViewById(R.id.confirm);
      Button btn3 = (Button)view.findViewById(R.id.Face_Upload);
      Button btn4 = (Button)view.findViewById(R.id.setting);

      Session_key = getArguments().getString(mKEY); // 값 들어옴 확인o
      //Toast.makeText(context,Session_key,Toast.LENGTH_LONG).show(); // 세션값 들어옴 확인o

      mWebView = (WebView)view.findViewById(R.id.Kangwon);
      mWebView.setWebViewClient(new WebViewClient());
      mWebSettings = mWebView.getSettings();
      mWebSettings.setJavaScriptEnabled(true);
      mWebView.loadUrl("https://www.kangwon.ac.kr/www/index.do");

      btn1.setOnClickListener(new View.OnClickListener() {  //check 에 대한 버튼 기능 추가
          @Override
          public void onClick(View v) {
              String Send_Session_key = Session_key; // 값 들어옴 확인o
              //Toast.makeText(context,Send_Session_key,Toast.LENGTH_LONG).show();
              Intent intent = new Intent(getActivity(), select_menuF.class); //
              intent.putExtra("Session_key", Send_Session_key);
              startActivity(intent);
          }
      });

      btn2.setOnClickListener(new View.OnClickListener() { // confirm에 대한 버튼 기능 추가
          @Override
          public void onClick(View v) {
              Intent intent = new Intent(getActivity(), confirm_the_attendF.class);
              startActivity(intent);
          }
      });


       btn3.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
              Intent intent = new Intent(getActivity(), select_menuF.class); //
              startActivity(intent);
          }
      });

      /*
      btn4.setOnClickListener(new View.OnClickListener() {  //check 에 대한 버튼 기능 추가
          @Override
          public void onClick(View v) {
              Intent intent = new Intent(getActivity(), take_photoF.class); //
              startActivity(intent);
          }
      });
      */

    return view;
    }
}
