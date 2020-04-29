package com.example.myapplication;

import android.app.Application;

public class MyApllication extends Application {

    private String mGlobalString;

    public String getGlobalString()
    {
        return mGlobalString;
    }
    public void setGlobalString(String globalString)
    {
        this.mGlobalString = globalString;
    }
}
//위의 MyApllication은 Activity간에서만 사용 가능한 것인가?