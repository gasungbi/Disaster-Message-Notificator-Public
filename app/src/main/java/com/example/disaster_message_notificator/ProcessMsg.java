package com.example.disaster_message_notificator;

import java.util.ArrayList;
import java.util.Iterator;

public class ProcessMsg {
    private ArrayList<MsgInfo> msgInfo;
    private String location;

    public ProcessMsg() {
        msgInfo = new ArrayList<MsgInfo>();

        location = "전국"; // 초기값은 '전국'
    }

    public void updateMarkedMsgInfo() {
        // 클릭한 마커에 대한 메시지 목록으로 갱신
        ArrayList<MsgInfo> tempMsgInfo = new ArrayList<MsgInfo>();
        if(!location.equals("전국")) {
            for(MsgInfo entity : msgInfo) {
                if(entity.getLocation_name().contains(location)) {
                    tempMsgInfo.add(entity);
                }
            }

            msgInfo = tempMsgInfo;
        }
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setMsgInfo(ArrayList<MsgInfo> msgInfo) {
        this.msgInfo = msgInfo;
    }

    public ArrayList<MsgInfo> getMsgInfo() {
        ArrayList<MsgInfo> deepCopy = new ArrayList<>(msgInfo.size());
        Iterator<MsgInfo> iterator = msgInfo.iterator();
        while(iterator.hasNext()) {
            deepCopy.add(iterator.next().clone());
        }

        return deepCopy;
    }
}
