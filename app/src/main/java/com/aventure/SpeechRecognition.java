package com.aventure;

import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.widget.Toast;

import java.util.Locale;

public class SpeechRecognition {

    private SpeechRecognizer mSpeechRecognizer;
    private boolean mIsListening = false;
    private AudioManager am;
    private Context context;

    public SpeechRecognition(Context context)
    {
        this.context = context;
        mSpeechRecognizer = SpeechRecognizer.createSpeechRecognizer(context);
        am = (AudioManager)context.getSystemService(Context.AUDIO_SERVICE);
    }

    public void prompt(SpeechRecognitionListener speechRecognitionListener) {

        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);

        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());

        mSpeechRecognizer.setRecognitionListener(speechRecognitionListener);

        if(!mIsListening) {
            mIsListening = true;
            mute();
            mSpeechRecognizer.startListening(intent);
        }
    }

    public void unLock() {
        mIsListening = false;
    }

    public void mute() {
        //am.setStreamMute(AudioManager.STREAM_SYSTEM, true);
    }

    public void unMute() {
        //am.setStreamMute(AudioManager.STREAM_SYSTEM, false);
    }

    public void cancel() {
        mSpeechRecognizer.cancel();
        unLock();
    }


    public void destroy()
    {
        mSpeechRecognizer.destroy();
    }

    public void toast(String s) {
        Toast.makeText(context, s, Toast.LENGTH_SHORT).show();
    }
}
