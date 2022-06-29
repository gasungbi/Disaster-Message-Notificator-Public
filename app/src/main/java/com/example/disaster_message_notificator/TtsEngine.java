package com.example.disaster_message_notificator;


import android.speech.tts.TextToSpeech;
import java.util.Locale;

import static android.speech.tts.TextToSpeech.ERROR;

public class TtsEngine {
    protected TextToSpeech tts;

    public TtsEngine() {
        tts = new TextToSpeech(((MainActivity)MainActivity.mContext), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int i) {
                if(i != ERROR){
                    tts.setLanguage(Locale.KOREAN);
                }
            }
        });
    }

    public void speakTTS(String text){
        tts.speak(text,TextToSpeech.QUEUE_FLUSH,null);
//        ttsDestroy();
    }

    public void ttsDestroy(){
        if(tts!=null){
            tts.stop();
//            tts.shutdown();
//            tts = null;
        }

    }
}