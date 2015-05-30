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
        stories.add("castle");

        show();
    }

    private void show() {

        // Buttons
        ArrayList<Button> buttons = new ArrayList<>();
        if(startFrom == 0) {
            buttons.add((Button) findViewById(R.id.buttonA));
        }
        buttons.add((Button) findViewById(R.id.buttonB));
        buttons.add((Button) findViewById(R.id.buttonC));

        int j = startFrom;
        for (int i = 0; i < buttons.size(); i++) {
            Button b = buttons.get(i);

            if(j < stories.size()) {
                final int k = j;
                b.setText(stories.get(j));
                b.setTypeface(null, Typeface.BOLD);
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
                b.setText(null);
                b.setOnClickListener(null);
            }
        }

        // Previous
        if(startFrom != 0){
            Button bA = ((Button) findViewById(R.id.buttonA));
            bA.setText(getString(R.string.previous));
            bA.setTypeface(null, Typeface.NORMAL);
            bA.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    previousPage();
                }
            });
        }

        // Next
        Button bD = ((Button) findViewById(R.id.buttonD));
        if(     (startFrom == 0 && startFrom + 3 < stories.size()) ||
                (startFrom >  0 && startFrom + 2 < stories.size()))
        {
            bD.setText(getString(R.string.next));
            bD.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    nextPage();
                }
            });
        }
        else
        {
            bD.setText("");
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
        startFrom -= 2;

        if(startFrom < 0)
            startFrom = 0;

        show();
    }


}
