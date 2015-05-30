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

    private SpeechRecognizer mSpeechRecognizer;
    private boolean mIsListening = false;

    public SpeechRecognition(Context context)
    {
        mSpeechRecognizer = SpeechRecognizer.createSpeechRecognizer(context);
    }

    public void prompt(SpeechRecognitionListener speechRecognitionListener) {

        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);

        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());

        mSpeechRecognizer.setRecognitionListener(speechRecognitionListener);

        if(!mIsListening) {
            mIsListening = true;
            mSpeechRecognizer.startListening(intent);
        }
    }

    public void unLock() {
        mIsListening = false;
    }

    public void cancel() {
        mSpeechRecognizer.cancel();
    }

    public void destroy()
    {
        mSpeechRecognizer.destroy();
    }

}
