package com.example.disaster_message_notificator;

import android.content.Context;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class SettingActivity extends AppCompatActivity {
    public static Context sContext;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        setTitle("설정");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        sContext = this;
//        getFragmentManager().beginTransaction()
//                .replace(android.R.id.content, new SettingPreferenceFragment())
//                .commit();
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
        }
        return super.onOptionsItemSelected(item);
    }

//    @Override
//    protected void onDestroy() {
//        setResult(RESULT_CANCELED);
//        finish();
//        super.onDestroy();
//    }
}
