package com.example.noisybirds.util;import android.app.Application;import io.paperdb.Paper;public class NosiyApplication extends Application {    @Override    public void onCreate() {        super.onCreate();        Paper.init(this);    }}