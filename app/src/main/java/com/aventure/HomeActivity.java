package com.aventure;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;


public class HomeActivity extends Activity {

    private static final String TAG = "HomeActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);
    }

    // Start
    public void clickA(View v){
        Log.d(TAG, "clickA");
        Intent intent = new Intent(getApplicationContext(), StoriesActivity.class);
        startActivity(intent);
    }

    // Buy
    public void clickB(View v){
        Log.d(TAG, "clickB");
        Toast.makeText(this, "Not yet implemented", Toast.LENGTH_SHORT).show();
    }

    // Home
    /*public void clickC(View v){
        Log.d(TAG, "clickC");
        setResult(HOME);
        finish();
    }

    public void clickD(View v){
        Log.d(TAG, "clickD");
        finish();
    }*/
}
