package com.aventure;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.io.IOException;
import java.util.ArrayList;


public class HomeActivity extends Activity {

    private ArrayList<String> stories = new ArrayList<>();
    private int startFrom = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);

        // Stories
        stories.add("42");
        stories.add("Castle");
        stories.add("Castle");
        stories.add("42");

        show();
    }

    private void show() {

        // Buttons
        ArrayList<Tile> buttons = new ArrayList<>();
        if(startFrom == 0) {
            buttons.add((Tile) findViewById(R.id.buttonA));
        }
        buttons.add((Tile) findViewById(R.id.buttonB));
        buttons.add((Tile) findViewById(R.id.buttonC));

        int j = startFrom;
        for (int i = 0; i < buttons.size(); i++) {
            Tile b = buttons.get(i);

            if(j < stories.size()) {
                final int k = j;
                b.setText(stories.get(j));
                b.setBackText("");
                //b.setTypeface(Typeface.BOLD);
                b.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getApplicationContext(), GameActivity.class);
                        intent.putExtra(GameActivity.STORY, stories.get(k));
                        startActivity(intent);
                    }
                });
                j += 1;
            }
            else
            {
                b.clear();
                b.setOnClickListener(null);
            }
        }

        // Previous
        if(startFrom != 0){
            Tile bA = ((Tile) findViewById(R.id.buttonA));
            bA.setText("<");
            bA.setBackText("");
            //bA.setTypeface(Typeface.NORMAL);
            bA.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    previousPage();
                }
            });
        }

        // Next
        Tile bD = ((Tile) findViewById(R.id.buttonD));
        if(     (startFrom == 0 && startFrom + 3 < stories.size()) ||
                (startFrom >  0 && startFrom + 2 < stories.size()))
        {
            bD.setText(">");
            bD.setBackText("");
            bD.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    nextPage();
                }
            });
        }
        else
        {
            bD.clear();
            bD.setOnClickListener(null);
        }

    }

    public void nextPage(){
        if(startFrom == 0) {
            startFrom += 3;
        }
        else
        {
            startFrom += 2;
        }
        // TODO limite
        show();
    }

    public void previousPage(){
        if(startFrom <= 3) {
            startFrom -= 3;
        }
        else
            startFrom -= 2;

        if(startFrom < 0)
            startFrom = 0;

        show();
    }


}
