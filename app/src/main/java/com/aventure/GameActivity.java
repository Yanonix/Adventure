package com.aventure;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Locale;


public class GameActivity extends Activity {

    public static String STORY = "GameActivity.story";

    StoryEngine engine;
    ArrayList<Button> buttons;
    String[] letters = {"A","B","C"};

    private TextToSpeech voice;
    private SpeechRecognition mSpeechRecognition;
    private UtteranceProgressListener utteranceProgressListener;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game);

        // Buttons
        buttons = new ArrayList<>();
        buttons.add((Button) findViewById(R.id.buttonA));
        buttons.add((Button) findViewById(R.id.buttonB));
        buttons.add((Button) findViewById(R.id.buttonC));

        (((Button) findViewById(R.id.buttonMenu))).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GameActivity.this, PauseActivity.class);
                startActivity(intent);
            }
        });

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

        // Voice
        mSpeechRecognition = new SpeechRecognition(this);
        utteranceProgressListener = new UtteranceProgressListener(){
            @Override
            public void onStart(String utteranceId) {
            }

            @Override
            public void onError(String utteranceId) {
            }

            @Override
            public void onDone(String utteranceId) {
                if(utteranceId.equals("end"))
                {
                    View v = findViewById(R.id.buttonA);
                    v.post(new Runnable() {
                        public void run() {
                            mSpeechRecognition.prompt(new SpeechRecognitionListener(mSpeechRecognition) {
                                @Override
                                public void onResult(String result) {
                                    if (result != null)
                                    {
                                        Toast.makeText(getApplicationContext(), result, Toast.LENGTH_SHORT).show();

                                        result = result.toLowerCase();
                                        boolean available = false;
                                        boolean valid = false;

                                        if(     result.equals("a")      ||
                                                result.equals("hey")    ||
                                                result.equals("first")  ||
                                                result.equals("one")    ||
                                                result.equals("yellow") ||
                                                result.equals("orange"))
                                        {
                                            valid = true;
                                            available = engine.makeChoice(0);
                                        }
                                        else if(result.equals("b")      ||
                                                result.equals("d")      ||
                                                result.equals("second") ||
                                                result.equals("two")    ||
                                                result.equals("green"))
                                        {
                                            valid = true;
                                            available = engine.makeChoice(1);
                                        }
                                        else if(result.equals("c")      ||
                                                result.equals("third")  ||
                                                result.equals("tree")   ||
                                                result.equals("red"))
                                        {
                                            valid = true;
                                            available = engine.makeChoice(2);
                                        }
                                        else if(result.equals("repeat") ||
                                                result.equals("repeats"))
                                        {
                                            fillQuestions();
                                        }
                                        else if(result.equals("exit")    ||
                                                result.equals("bye bye") ||
                                                result.equals("goodbye") ||
                                                result.equals("shut up") ||
                                                result.equals("f*** you"))
                                        {
                                            voice.speak("Bye, see you !", TextToSpeech.QUEUE_FLUSH, null, null);
                                        }
                                        else
                                        {
                                            voice.speak("Can you repeat ?", TextToSpeech.QUEUE_FLUSH, null, null);
                                            voice.playSilentUtterance(1, TextToSpeech.QUEUE_ADD, "end");
                                        }

                                        if(valid)
                                        {
                                            if(available)
                                                fillQuestions();
                                            else
                                            {
                                                voice.speak("This answer is not valid", TextToSpeech.QUEUE_FLUSH, null, null);
                                                voice.playSilentUtterance(1, TextToSpeech.QUEUE_ADD, "end");
                                            }

                                        }

                                    }
                                    else
                                    {
                                        voice.speak("I heard nothing, can you repeat ?", TextToSpeech.QUEUE_FLUSH, null, null);
                                        voice.playSilentUtterance(1, TextToSpeech.QUEUE_ADD, "end");
                                    }

                                }
                            });
                        }
                    });
                }
            }
        };

        voice = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status != TextToSpeech.ERROR) {
                    voice.setLanguage(Locale.US);
                    voice.setOnUtteranceProgressListener(utteranceProgressListener);

                    // Start the game
                    try {
                        engine = new StoryEngine(getAssets().open(getIntent().getStringExtra(STORY) + ".xml"));
                        fillQuestions();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });


    }

    private void fillQuestions() {
        // Situations
        final StoryEngine.Situation situation = engine.getSituation();

        // Toast.makeText(this, situation.text, Toast.LENGTH_SHORT).show();

        for (int i = 0; i < situation.texts.size(); i++) {
            if(situation.texts.get(i).wait > 0) {
                voice.playSilentUtterance(situation.texts.get(i).wait, TextToSpeech.QUEUE_ADD, null);
            }
            voice.speak(situation.texts.get(i).text, TextToSpeech.QUEUE_FLUSH, null, null);
        }

        // Reset buttons
        for(Button b : buttons) {
            b.setText("");
        }

        // Choices
        for (int i = 0; i < situation.choices.size(); i++) {
            StoryEngine.Choice choice = situation.choices.get(i);
            String letter = letters[i];

            // Set button
            buttons.get(i).setText(choice.text);

            // Set voice
            voice.playSilentUtterance(700, TextToSpeech.QUEUE_ADD, null);
            voice.speak(letter + ". " + choice.text, TextToSpeech.QUEUE_ADD, null, null);
        }

        // End of speech
        voice.playSilentUtterance(1, TextToSpeech.QUEUE_ADD, "end");

    }

    @Override
    protected void onDestroy() {
        mSpeechRecognition.destroy();
        voice.stop();
        voice.shutdown();

        super.onDestroy();
    }
}
