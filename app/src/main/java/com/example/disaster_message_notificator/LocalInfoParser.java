package com.example.disaster_message_notificator;

import android.util.Log;
import org.xmlpull.v1.XmlPullParser;
import java.util.ArrayList;

public class LocalInfoParser {
    private ArrayList<LocalInfo> localInfos;

    public LocalInfoParser() {
        localInfos = new ArrayList<LocalInfo>();

        readXml();
    }

    public void readXml() {
        boolean local_name_Flag = false, latitude_Flag = false, longitude_Flag = false;
        String local_name = null, latitude = null, longitude = null;

        try{
            XmlPullParser parser = ((MainActivity)MainActivity.mContext).getResources().getXml(R.xml.local_info);

            int parserEvent = parser.getEventType();

            while (parserEvent != XmlPullParser.END_DOCUMENT) {
                switch (parserEvent) {
                    case XmlPullParser.START_TAG://parser가 시작 태그를 만나면 실행
                        if (parser.getName().equals("local_name")) { //local_name 태그를 만나면 활성화
                            local_name_Flag = true;
                        }
                        if (parser.getName().equals("latitude")) { //latitude 태그를 만나면 활성화
                            latitude_Flag = true;
                        }
                        if (parser.getName().equals("longitude")) { //longitude 태그를 만나면 활성화
                            longitude_Flag = true;
                        }
                        break;

                    case XmlPullParser.TEXT:// parser가 내용에 접근했을때
                        if (local_name_Flag) { // local_name_Flag이 true일 때 태그의 내용을 저장.
                            local_name = parser.getText();
                            local_name_Flag = false;
                        }
                        if (latitude_Flag) { // latitude_Flag이 true일 때 태그의 내용을 저장.
                            latitude = parser.getText();
                            latitude_Flag = false;
                        }
                        if (longitude_Flag) { // longitude_Flag이 true일 때 태그의 내용을 저장.
                            longitude = parser.getText();
                            longitude_Flag = false;
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        if (parser.getName().equals("row")) {
                            // LocalInfo 객체를 만들고, 하나의 메시지를 파싱한 결과를 종합한 후 해당 MsgInfo를 msgInfo 어레이 리스트에 추가
                            LocalInfo entity = new LocalInfo();
                            entity.setLocal_name(local_name);
                            entity.setLatitude(latitude);
                            entity.setLongitude(longitude);
                            localInfos.add(entity);
                        }
                        break;
                }
                parserEvent = parser.next();
            }
            Log.d("customCheck", "로컬정보 파싱 성공");
        } catch(Exception e){
            e.printStackTrace();
            Log.d("customCheck", "로컬정보 파싱 오류 발생");
        }
    }

    public ArrayList<LocalInfo> getLocalInfos() {
        return localInfos;
    }
}
