package com.example.disaster_message_notificator;

public class MsgInfo implements Cloneable {
    private String create_date; //생성 시간
    private String location_name; //지역 이름
    private String msg; //메시지 내용

    public MsgInfo() {
        super();
    }

    public MsgInfo(String create_date, String location_name, String msg) {
        this.create_date = create_date;
        this.location_name = location_name;
        this.msg = msg;
    }

    public String getCreate_date() {
        return create_date;
    }

    public void setCreate_date(String create_date) {
        this.create_date = create_date;
    }

    public String getLocation_name() {
        return location_name;
    }

    public void setLocation_name(String location_name){
        this.location_name = location_name;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public MsgInfo clone() {
        // 깊은 복사
        // 참고 : https://ahjm-note.tistory.com/6
        MsgInfo clone = null;
        try {
            clone = (MsgInfo) super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return clone;
    }
}
