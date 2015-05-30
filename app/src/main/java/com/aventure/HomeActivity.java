package com.aventure;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;


public class HomeActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);
    }

    public void loadA(View v){
        Intent intent = new Intent(this, GameActivity.class);
        intent.putExtra(GameActivity.STORY, "42");
        startActivity(intent);
    }

    public void loadB(View v){
        Intent intent = new Intent(this, GameActivity.class);
        intent.putExtra(GameActivity.STORY, "castle");
        startActivity(intent);
    }

    public void loadC(View v){
        Intent intent = new Intent(this, GameActivity.class);
        intent.putExtra(GameActivity.STORY, "mirror");
        startActivity(intent);
    }

}
