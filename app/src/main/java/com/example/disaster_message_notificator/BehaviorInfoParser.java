package com.example.disaster_message_notificator;

import android.util.Log;
import org.xmlpull.v1.XmlPullParser;
import java.util.ArrayList;

public class BehaviorInfoParser {
    private ArrayList<BehaviorInfo> behaviorInfos;

    public BehaviorInfoParser() {
        behaviorInfos = new ArrayList<BehaviorInfo>();

        readXml();
    }

    public void readXml() {
        boolean disaster_keyword_Flag = false, behavior_example_link_Flag = false;
        String disaster_keyword = null, behavior_example_link = null;

        try{
            XmlPullParser parser = ((MainActivity)MainActivity.mContext).getResources().getXml(R.xml.behavior_info);

            int parserEvent = parser.getEventType();

            while (parserEvent != XmlPullParser.END_DOCUMENT) {
                switch (parserEvent) {
                    case XmlPullParser.START_TAG://parser가 시작 태그를 만나면 실행
                        if (parser.getName().equals("disaster_keyword")) { //생성 날짜 태그를 만나면 활성화
                            disaster_keyword_Flag = true;
                        }
                        if (parser.getName().equals("behavior_example_link")) { //지역 이름 태그를 만나면 활성화
                            behavior_example_link_Flag = true;
                        }
                        break;

                    case XmlPullParser.TEXT:// parser가 내용에 접근했을때
                        if (disaster_keyword_Flag) { // Create_date_Flag이 true일 때 태그의 내용을 저장.
                            disaster_keyword = parser.getText();
                            disaster_keyword_Flag = false;
                        }
                        if (behavior_example_link_Flag) { // Location_name_Flag이 true일 때 태그의 내용을 저장.
                            behavior_example_link = parser.getText();
                            behavior_example_link_Flag = false;
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        if (parser.getName().equals("row")) {
                            // MsgInfo 객체를 만들고, 하나의 메시지를 파싱한 결과를 종합한 후 해당 MsgInfo를 msgInfo 어레이 리스트에 추가
                            BehaviorInfo entity = new BehaviorInfo();
                            entity.setDisaster_keyword(disaster_keyword);
                            entity.setBehavior_example_link(behavior_example_link);
                            behaviorInfos.add(entity);
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

    public ArrayList<BehaviorInfo> getBehaviorInfos() {
        return behaviorInfos;
    }
}
