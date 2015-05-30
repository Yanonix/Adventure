package com.aventure;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;


public class PauseActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pause);
    }

    public void clickA(View v){

    }

    public void clickB(View v){

    }

    public void clickC(View v){

    }

    public void clickD(View v){
        finish();
    }
}
