package com.aventure;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;


public class PauseActivity extends Activity {

    private static final String TAG = "PauseActivity";
    public static final int REPEAT = 101;
    public static final int UNDO = 102;
    public static final int HOME = 103;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pause);
    }

    // Repeat
    public void clickA(View v){
        Log.d(TAG, "clickA");
        setResult(REPEAT);
        finish();
    }

    // Undo
    public void clickB(View v){
        Log.d(TAG, "clickB");
        setResult(UNDO);
        finish();
    }

    // Home
    public void clickC(View v){
        Log.d(TAG, "clickC");
        setResult(HOME);
        finish();
    }

    public void clickD(View v){
        Log.d(TAG, "clickD");
        finish();
    }
}
