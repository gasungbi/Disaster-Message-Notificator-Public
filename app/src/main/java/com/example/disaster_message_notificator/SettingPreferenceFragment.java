package com.example.disaster_message_notificator;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.MultiSelectListPreference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.preference.PreferenceScreen;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.google.android.material.snackbar.Snackbar;

import java.util.Iterator;
import java.util.Set;

public class SettingPreferenceFragment extends PreferenceFragment {

    private SharedPreferences prefs;
    private ListPreference msgNumberPreference;

    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.settings_preference);
        msgNumberPreference = (ListPreference)findPreference("msg_number_list");

        prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());

        msgNumberPreference.setDefaultValue("300");

        if(!prefs.getString("msg_number_list", "").equals("")){
            msgNumberPreference.setSummary(prefs.getString("msg_number_list", "300"));
        }

//        addPreferencesFromResource(R.xml.settings_preference);
        prefs.registerOnSharedPreferenceChangeListener(prefListener);
    }

    @Override
    public void onResume() {
        super.onResume();
        getPreferenceManager().getSharedPreferences().registerOnSharedPreferenceChangeListener(prefListener);
    }

    @Override
    public void onPause() {
        super.onPause();
        getPreferenceManager().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(prefListener);
    }

    private SharedPreferences.OnSharedPreferenceChangeListener prefListener = new SharedPreferences.OnSharedPreferenceChangeListener() {

        public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
            if(key.equals("msg_number_list")){
                msgNumberPreference.setSummary(prefs.getString("msg_number_list", ""));

//                Toast.makeText(getActivity(), "수신 문자량 설정 : "+ prefs.getString("msg_number_list", ""), Toast.LENGTH_SHORT).show(); // 옵션값 확인

                ((MainActivity)MainActivity.mContext).mainModel.changeSetting(prefs.getString("msg_number_list", ""));
//                ((MainActivity)MainActivity.mContext).getMsg();

//                Toast.makeText(getActivity(), "수신 문자량 값 변경됨", Toast.LENGTH_SHORT).show();    // 옵션값 확인
                if(Integer.parseInt(prefs.getString("msg_number_list", "")) >= 400) {
                    final Snackbar sb = Snackbar.make(((SettingActivity)SettingActivity.sContext).findViewById(R.id.clSetting),
                            "문자 수신량이 너무 많으면 데이터 사용량이나 로딩 시간이 늘어날 수 있습니다.", Snackbar.LENGTH_INDEFINITE)
                            .setGestureInsetBottomIgnored(true);
                    sb.setAction("확인", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            sb.dismiss();
                        }
                    });
                    sb.show();
                } else {
//                    Snackbar.make(((SettingActivity)SettingActivity.sContext).findViewById(R.id.clSetting),
//                            "문자 수신량이 " + prefs.getString("msg_number_list", "") + "건으로 변경되었습니다.", Snackbar.LENGTH_SHORT)
//                            .setGestureInsetBottomIgnored(true).show();
                }
            }
        }
    };

}