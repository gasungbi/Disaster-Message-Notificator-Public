package com.example.disaster_message_notificator;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class LisenceActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lisence);
        setTitle("라이선스");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        TextView tvLisenceInfoTitle = (TextView)findViewById(R.id.tvLisenceInfoTitle);
        tvLisenceInfoTitle.setText("사용된 소프트웨어");

        TextView tvLisenceInfoMsg = (TextView)findViewById(R.id.tvLisenceInfoMsg);
        tvLisenceInfoMsg.setText("재난문자 알림이 앱은 아래의 API 및 라이브러리를 활용하여 만들어졌습니다.\n\n이미지를 클릭하면 관련 사이트로 이동합니다.");

        ImageView ivNaverOpenApi = (ImageView) findViewById(R.id.ivNaverOpenApi);
        ivNaverOpenApi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://developers.naver.com")));
            }
        });

        ImageView ivGlide = (ImageView) findViewById(R.id.ivGlide);
        ivGlide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://bumptech.github.io/glide/")));
            }
        });
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
