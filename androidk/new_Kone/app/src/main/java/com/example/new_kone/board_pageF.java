package com.example.new_kone;

//import android.support.annotation.Nullable;
//import android.support.v4.app.Fragment;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class board_pageF extends Fragment {

    WebView mWebView;
    WebSettings mWebSettings;

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

        View view = inflater.inflate(R.layout.board_page,container,false);

        mWebView = (WebView)view.findViewById(R.id.webview2);

        mWebView.setWebViewClient(new WebViewClient());
        mWebSettings = mWebView.getSettings();
        mWebSettings.setJavaScriptEnabled(true);

        mWebView.loadUrl("https://www.kangwon.ac.kr/www/selectBbsNttList.do?bbsNo=81&key=277&searchCtgry=%EC%B6%98%EC%B2%9C&");




        return view;
    }
}
