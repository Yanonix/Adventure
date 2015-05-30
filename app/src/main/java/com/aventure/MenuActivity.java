package com.aventure;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;


public class MenuActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
    }

    public void loadA(View v){
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra(MainActivity.STORY, "42");
        startActivity(intent);
    }

    public void loadB(View v){
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra(MainActivity.STORY, "castle");
        startActivity(intent);
    }

}
