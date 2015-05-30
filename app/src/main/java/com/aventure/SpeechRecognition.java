package com.aventure;

import android.content.Context;
import android.content.Intent;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;

import java.util.Locale;

/**
 * Created by yanonix on 30/05/15.
 */
public class SpeechRecognition {

    private Context context;
    private SpeechRecognizer mSpeechRecognizer;

    public SpeechRecognition(Context _context)
    {
        context = _context;
        mSpeechRecognizer = SpeechRecognizer.createSpeechRecognizer(context);
    }

    public void prompt(SpeechRecognitionListener speechRecognitionListener) {

        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);

        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());

        //intent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, this.getPackageName());

        mSpeechRecognizer.setRecognitionListener(speechRecognitionListener);

        mSpeechRecognizer.startListening(intent);
    }

    public void destroy()
    {
        mSpeechRecognizer.destroy();
    }

}
