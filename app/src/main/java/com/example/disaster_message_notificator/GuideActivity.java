package com.example.disaster_message_notificator;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class GuideActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);
        setTitle("도움말");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        TextView tvGuideMsg = (TextView)findViewById(R.id.tvGuideMsg);
        tvGuideMsg.setText("재난 문자 알림이는 행정안전부가 제공하는 재난문자 현황을 지도를 함께 보여주어 전국의 재난 정보를 쉽게 확인할 수 있습니다." +
                "                \n\n재난문자 현황은 사용자가 지정한 문자 수신량만큼 최근순으로 표시됩니다." +
                "                \n\n실시간 정보는 제공하지 않습니다. 재난문자 송출 직후의 실시간 정보는 국민재난안전포털(http://www.safekorea.go.kr)을 확인하세요." +
                "                \n\n사용 방법은 아래의 내용을 참고하세요.");
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
        }
        return super.onOptionsItemSelected(item);
    }
}