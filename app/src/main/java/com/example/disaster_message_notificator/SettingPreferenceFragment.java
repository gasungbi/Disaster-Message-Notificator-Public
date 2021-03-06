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

//                Toast.makeText(getActivity(), "?????? ????????? ?????? : "+ prefs.getString("msg_number_list", ""), Toast.LENGTH_SHORT).show(); // ????????? ??????

                ((MainActivity)MainActivity.mContext).mainModel.changeSetting(prefs.getString("msg_number_list", ""));
//                ((MainActivity)MainActivity.mContext).getMsg();

//                Toast.makeText(getActivity(), "?????? ????????? ??? ?????????", Toast.LENGTH_SHORT).show();    // ????????? ??????
                if(Integer.parseInt(prefs.getString("msg_number_list", "")) >= 400) {
                    final Snackbar sb = Snackbar.make(((SettingActivity)SettingActivity.sContext).findViewById(R.id.clSetting),
                            "?????? ???????????? ?????? ????????? ????????? ??????????????? ?????? ????????? ????????? ??? ????????????.", Snackbar.LENGTH_INDEFINITE)
                            .setGestureInsetBottomIgnored(true);
                    sb.setAction("??????", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            sb.dismiss();
                        }
                    });
                    sb.show();
                } else {
//                    Snackbar.make(((SettingActivity)SettingActivity.sContext).findViewById(R.id.clSetting),
//                            "?????? ???????????? " + prefs.getString("msg_number_list", "") + "????????? ?????????????????????.", Snackbar.LENGTH_SHORT)
//                            .setGestureInsetBottomIgnored(true).show();
                }
            }
        }
    };

}