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
    public static int PAUSE_MENU = 101;

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
                Intent intent = new Intent(getApplicationContext(), PauseActivity.class);
                startActivityForResult(intent, PAUSE_MENU);
            }
        });

        for (int i = 0; i < buttons.size(); i++) {
            final int j = i;
            Button b = buttons.get(i);
            b.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(engine.makeChoice(j)) {
                        fillQuestions();
                    }
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

                                        if(result.contains("exit")    ||
                                                result.contains("bye bye") ||
                                                result.contains("goodbye") ||
                                                result.contains("shut up") ||
                                                result.contains("f*** you"))
                                        {
                                            voice.speak("Bye, see you !", TextToSpeech.QUEUE_FLUSH, null, null);
                                            finish();
                                        }
                                        else if(result.contains("brake")    ||
                                                result.contains("pause") ||
                                                result.contains("wait"))
                                        {
                                            voice.speak("Pause", TextToSpeech.QUEUE_FLUSH, null, null);
                                            Intent intent = new Intent(getApplicationContext(), PauseActivity.class);
                                            startActivityForResult(intent, PAUSE_MENU);
                                        }
                                        else if(result.equals("a")      ||
                                                result.equals("hey")    ||
                                                result.contains("first")  ||
                                                result.equals("one")    ||
                                                result.contains("yellow") ||
                                                result.equals("orange"))
                                        {
                                            valid = true;
                                            available = engine.makeChoice(0);
                                        }
                                        else if(result.equals("b")      ||
                                                result.equals("d")      ||
                                                result.contains("second") ||
                                                result.equals("two")    ||
                                                result.contains("green"))
                                        {
                                            valid = true;
                                            available = engine.makeChoice(1);
                                        }
                                        else if(result.equals("c")      ||
                                                result.contains("third")  ||
                                                result.equals("tree")   ||
                                                result.contains("blue"))
                                        {
                                            valid = true;
                                            available = engine.makeChoice(2);
                                        }
                                        else if(result.contains("repeat") ||
                                                result.contains("repeats")) {
                                            fillQuestions();
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PAUSE_MENU) {
            if (resultCode == PauseActivity.REPEAT) {
                fillQuestions();
            }
            else if (resultCode == PauseActivity.UNDO) {
                engine.undo();
                fillQuestions();
            }
            else if (resultCode == PauseActivity.HOME) {
                finish();
            }
        }
    }

    @Override
    protected void onDestroy() {
        mSpeechRecognition.destroy();
        //voice.stop();
        voice.shutdown();

        super.onDestroy();
    }

    @Override
    protected void onStop() {
        mSpeechRecognition.cancel();
        voice.stop();

        super.onStop();

    }

}