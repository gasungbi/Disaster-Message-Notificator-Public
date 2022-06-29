package com.example.disaster_message_notificator;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import java.util.ArrayList;

public class Setting {
    private String msgNumber;
    private ArrayList<LocalInfo> localInfos;
    private ArrayList<BehaviorInfo> behaviorInfos;
    private SharedPreferences prefs;

    public Setting() {
        prefs = PreferenceManager.getDefaultSharedPreferences((MainActivity)MainActivity.mContext);
        msgNumber = prefs.getString("msg_number_list", "300");
        localInfos = new ArrayList<LocalInfo>();
        behaviorInfos = new ArrayList<BehaviorInfo>();
    }

    public void setLocalInfos(ArrayList<LocalInfo> localInfos) {
        this.localInfos = localInfos;
    }

    public ArrayList<LocalInfo> getLocalInfos() {
        return localInfos;
    }

    public void setBehaviorInfos(ArrayList<BehaviorInfo> behaviorInfos) {
        this.behaviorInfos = behaviorInfos;
    }

    public ArrayList<BehaviorInfo> getBehaviorInfos() {
        return behaviorInfos;
    }

    public void setMsgNumber(String msgNumber) {
        this.msgNumber = msgNumber;
    }

    public String getMsgNumber() {
        return msgNumber;
    }
}
