package com.example.disaster_message_notificator;

import android.util.Log;

import java.util.ArrayList;
import java.util.Iterator;

public class MainModel {
    private ArrayList<MsgInfo> msgInfo, processedMsgInfo;
    private DisasterApi disasterApi;
    private LocalInfoParser localInfoParser;
    private BehaviorInfoParser behaviorInfoParser;
    private ProcessMsg processMsg;
    protected Setting setting;
    protected TtsEngine ttsEngine;

    public MainModel() {
        createInit();
        setSettingLocalInfo();      // XML에서 읽어온 로컬정보를 설정 변수에 할당
        setSettingBehaviorInfo();   // XML에서 읽어온 행동요령을 설정 변수에 할당
        recieveDisasterApi();       // 앱 최초 실행시 재난문자 수신
    }

    private void createInit() {
        setting = new Setting();
        localInfoParser = new LocalInfoParser();
        behaviorInfoParser = new BehaviorInfoParser();
        msgInfo = new ArrayList<MsgInfo>();
        processedMsgInfo = new ArrayList<MsgInfo>();
        ttsEngine = new TtsEngine();

        processMsg = new ProcessMsg();
    }

    public void recieveDisasterApi() {
        /* 재난문자 API 수신
         *  (메인모델 생성시, 혹은 별도 요청시에만 작동) */

        Log.d("customCheck", "recieveDisasterApi() 시작");
        disasterApi = new DisasterApi(setting.getMsgNumber());
        disasterApi.execute();                                      // 3분자동수신 구현시, 이곳에서 요청을 반복해야함.
        Log.d("customCheck", "recieveDisasterApi() 완료");
    }

    public void refreshDisasterApi() {
        // 수신 + 메시지 종합 처리 + 메시지 목록 갱신
        // (새로고침 버튼 클릭시 작동)

        Log.d("customCheck", "refreshDisasterApi() 시작");
        recieveDisasterApi();   // 재난문자 API를 신규 수신
        Log.d("customCheck", "refreshDisasterApi() 완료");
    }

    public void processingMsg() {
        /* 메시지 종합 처리 */
        if(!disasterApi.getMsgInfo().isEmpty())  {
            setMsgInfo(disasterApi.getMsgInfo());
        }
        filteringMsg();
    }

    public void filteringMsg() {
        /* 메시지 분류 */

        processMsg.setMsgInfo(msgInfo);
        processMsg.updateMarkedMsgInfo();
        processedMsgInfo = processMsg.getMsgInfo();
    }

    public ArrayList<MsgInfo> showMsg() {
        /* 메시지 표시 */

        ArrayList<MsgInfo> deepCopy = new ArrayList<>(processedMsgInfo.size());
        Iterator<MsgInfo> iterator = processedMsgInfo.iterator();
        while(iterator.hasNext()) {
            deepCopy.add(iterator.next().clone());
        }

        return deepCopy;
    }

    public void changeSetting(String msgNum) {
        /* 설정 변경 */

        setting.setMsgNumber(msgNum);
        processingMsg();
    }

    public void countPerLocation() {
        /* 수신된 메시지들의 지역별 갯수를 카운트하기 */

        int[] tempMsgCnt = disasterApi.getMsgCount(setting.getLocalInfos());
        ((MainActivity)MainActivity.mContext).setMarkerColor(tempMsgCnt);
    }

    public void setSettingLocalInfo() {
        setting.setLocalInfos(localInfoParser.getLocalInfos());
    }

    public ArrayList<LocalInfo> getSettingLocalInfo() {
        return setting.getLocalInfos();
    }

    public void setSettingBehaviorInfo() {
        setting.setBehaviorInfos(behaviorInfoParser.getBehaviorInfos());
    }

    public ArrayList<BehaviorInfo> getSettingBehaviorInfo() {
        return setting.getBehaviorInfos();
    }

    // 재난문자 API 수신값 가져오기
    public void setMsgInfo(ArrayList<MsgInfo> msgInfo) {
        // 수신된 API 결과 반환
        this.msgInfo = msgInfo;
    }

    public void selectLocation(String location) {
        processMsg.setLocation(location);
    }
}
