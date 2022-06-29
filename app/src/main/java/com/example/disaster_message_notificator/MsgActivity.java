package com.example.disaster_message_notificator;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NavUtils;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.Locale;

import static android.speech.tts.TextToSpeech.ERROR;

public class MsgActivity extends AppCompatActivity {
    private TextView tvLocationName, tvCreateDate, tvMsg, tvBehaviorMsg;
    private String createDate, locationName, msg, behaviorMsg;
    private TtsEngine ttsEngine;
//    private TextToSpeech tts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_msg);
        setTitle("긴급 재난문자");
//        getSupportActionBar().setSubtitle("공공 안전 경보");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ttsEngine = ((MainActivity)MainActivity.mContext).mainModel.ttsEngine;
//        tts = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
//            @Override
//            public void onInit(int i) {
//                if(i != ERROR){
//                    tts.setLanguage(Locale.KOREAN);
//                }
//            }
//        });
        initMsg();
//        Button button = findViewById(R.id.btnTTS);
//        button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if(!ttsEngine.tts.isSpeaking()){
//                    runTTS();
//                }
//            }
//        });

        FloatingActionButton fab = findViewById(R.id.fabTts);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!ttsEngine.tts.isSpeaking()){
                    runTTS();
                    Snackbar.make(view, "음성 출력을 시작합니다. 음성 출력을 종료하려면 TTS 버튼을 다시 누르세요.",
                            Snackbar.LENGTH_SHORT).setGestureInsetBottomIgnored(true).show();
                } else {
                    ttsEngine.ttsDestroy();
//                    ttsEngine.tts.stop();
                    Snackbar.make(view, "음성 출력을 종료합니다.",
                            Snackbar.LENGTH_SHORT).setGestureInsetBottomIgnored(true).show();
                }


            }
        });
//        tvMsg.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                runTTS();
//            }
//        });
    }

    @Override
    public void finish() {
        super.finish();
        ttsEngine.ttsDestroy();
//        ttsEngine.tts.stop();
    }

    public void initMsg() {
        tvCreateDate = (TextView) findViewById(R.id.tvCreateDate);
        tvLocationName = (TextView) findViewById(R.id.tvLocationName);
        tvMsg = (TextView) findViewById(R.id.tvMsg);
        tvBehaviorMsg = (TextView) findViewById(R.id.tvBehaviorMsg);

        Intent intent = getIntent();
        createDate = intent.getStringExtra("CreateDate");
        locationName = intent.getStringExtra("LocationName");
        msg = intent.getStringExtra("Msg");
        behaviorMsg = "";


        addBehaviorMsg();

        tvCreateDate.setText(createDate);
        tvLocationName.setText(locationName);
        tvMsg.setText(msg);
        tvBehaviorMsg.setText(behaviorMsg);
    }

    public void addBehaviorMsg() {
        ArrayList<BehaviorInfo> tempBehaviorInfos = ((MainActivity)MainActivity.mContext).mainModel.getSettingBehaviorInfo();

        for(int i = 0; i < tempBehaviorInfos.size(); i++) {
            if((msg.contains(tempBehaviorInfos.get(i).getDisaster_keyword())) && (!behaviorMsg.contains(tempBehaviorInfos.get(i).getBehavior_example_link()))) {
                behaviorMsg += tempBehaviorInfos.get(i).getBehavior_example_link()+"\n";
            }
        }

        if(behaviorMsg.length() != 0){
            behaviorMsg = behaviorMsg.substring(0,behaviorMsg.length()-1);
        } else {
             findViewById(R.id.cardBehaviorMsg).setVisibility(View.INVISIBLE);
        }
    }

    public void runTTS() {
        //TTS 엔진 실행
//        speakTTS(msg);
        ttsEngine.speakTTS(msg);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
        }
        return super.onOptionsItemSelected(item);
    }

//    public void speakTTS(String text){
//        tts.speak(text,TextToSpeech.QUEUE_FLUSH,null);
//    }
//
//    public void ttsDestroy(){
//        if(tts!=null){
//            tts.stop();
//            tts.shutdown();
//            tts = null;
//        }
//
//    }

//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
////        ttsDestroy();
////        ttsEngine.ttsDestroy();
//    }
}
