package com.aventure;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;


public class MainActivity extends Activity {

    StoryEngine engine;
    ArrayList<Button> buttons;
    String[] letters = {"A","B","C"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        buttons = new ArrayList<>();
        buttons.add((Button) findViewById(R.id.buttonA));
        buttons.add((Button) findViewById(R.id.buttonB));
        buttons.add((Button) findViewById(R.id.buttonC));

        for (int i = 0; i < buttons.size(); i++) {
            final int j = i;
            Button b = buttons.get(i);
            b.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    engine.makeChoice(j);
                    fillQuestions();
                }
            });
        }

        try {
            this.engine = new StoryEngine(getAssets().open("42.xml"));
            this.fillQuestions();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void fillQuestions() {
        StoryEngine.Situation situation = engine.getSituation();
        Toast.makeText(this, situation.text(), Toast.LENGTH_SHORT).show();
        for(Button b : buttons){
            b.setText("");
        }

        for (int i = 0; i < situation.choices.size(); i++) {
            StoryEngine.Choice choice = situation.choices.get(i);
            String letter = letters[i];
            buttons.get(i).setText(letter+"\n"+choice.text);
            //Toast.makeText(this, letter + ":" + choice.text, Toast.LENGTH_SHORT).show();
        }
    }

}
