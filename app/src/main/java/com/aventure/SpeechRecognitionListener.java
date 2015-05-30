package com.aventure;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.SpeechRecognizer;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by yanonix on 30/05/15.
 */
public class SpeechRecognitionListener implements RecognitionListener {

    public static String TAG = "SpeechRecognitionListener";
    public static String RESULTS = "SpeechRecognitionListener.Results";

    private SpeechRecognition mSpeechRecognition;

    public SpeechRecognitionListener(SpeechRecognition _mSpeechRecognition)
    {
        super();
        mSpeechRecognition = _mSpeechRecognition;

        Log.d(TAG, "constructor");
    }

    @Override
    public void onBeginningOfSpeech()
    {
        Log.d(TAG, "onBeginingOfSpeech");
    }

    @Override
    public void onBufferReceived(byte[] buffer)
    {

    }

    @Override
    public void onEndOfSpeech()
    {
        Log.d(TAG, "onEndOfSpeech");
    }

    @Override
    public void onError(int error)
    {
        //mSpeechRecognition.prompt(this);
        onResult(null);
        Log.d(TAG, "error = " + error);
    }

    @Override
    public void onEvent(int eventType, Bundle params)
    {

    }

    @Override
    public void onPartialResults(Bundle partialResults)
    {

    }

    @Override
    public void onReadyForSpeech(Bundle params)
    {
        Log.d(TAG, "onReadyForSpeech"); //$NON-NLS-1$
    }

    @Override
    public void onResults(Bundle results)
    {
        Log.d(TAG, "onResults"); //$NON-NLS-1$
        ArrayList<String> matches = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
        // matches are the return values of speech recognition engine
        // Use these values for whatever you wish to do
        onResult(matches.get(0));

        /*callbackIntent.putExtra(RESULTS, matches.get(0));
        context.sendBroadcast(callbackIntent);*/

        //Toast.makeText(context, matches.get(0), Toast.LENGTH_SHORT).show();
    }

    public void onResult(String result)
    {

    }

    @Override
    public void onRmsChanged(float rmsdB)
    {
    }
}
