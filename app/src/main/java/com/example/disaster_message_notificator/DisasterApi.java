package com.example.disaster_message_notificator;

import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.naver.maps.map.overlay.InfoWindow;
import com.naver.maps.map.overlay.Marker;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedInputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;

public class DisasterApi extends AsyncTask<ArrayList<MsgInfo>, Void, ArrayList<MsgInfo>> {
    private ArrayList<MsgInfo> msgInfo;
    private int[] locationMsgCnt;
    private String tempMsgNumber;

    public DisasterApi(String tempMsgNumber) {
        Log.d("customCheck", "DisasterApi 생성 완료");
        msgInfo = new ArrayList<MsgInfo>();
        this.tempMsgNumber = tempMsgNumber;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        // 신규 수신시, 기존 msgInfo를 비움.
        msgInfo.clear();
//        msgNumberTEMP = "300";
        Log.d("customCheck", "onPreExcute() 완료");
    }

    @Override
    protected ArrayList<MsgInfo> doInBackground(ArrayList<MsgInfo>... arrayLists) {
        requestApi(tempMsgNumber);  // 3분자동수신 구현시, 본 메소드가 실행되어야 함.

        Log.d("customCheck", "doInBackground() 완료");

        return msgInfo;
    }

    @Override
    protected void onPostExecute(ArrayList<MsgInfo> msgInfos) {
        super.onPostExecute(msgInfos);

        if(((MainActivity)MainActivity.mContext).customProgressDialog.isShowing()) {
            ((MainActivity)MainActivity.mContext).customProgressDialog.dismiss();
        }

        ((MainActivity)MainActivity.mContext).mainModel.countPerLocation();         // 메시지를 신규 수신하면, 갱신된 지역별 메시지 갯수를 다시 갱신한다.
        ((MainActivity)MainActivity.mContext).mainModel.processingMsg();            // 신규 수신된 메시지를 가져오고, 가공하기
        ((MainActivity)MainActivity.mContext).getMsg();                             // 메시지 표시(메시지 가져오기)

        ((MainActivity)MainActivity.mContext).infoWindow.setAdapter(new InfoWindow.DefaultTextAdapter( ((MainActivity)MainActivity.mContext)) {
            @NonNull
            @Override
            public CharSequence getText(@NonNull InfoWindow infoWindow) {
                return "재난문자 : " +  ((MainActivity)MainActivity.mContext).msgInfo.size() + "건";
            }
        });
        ((MainActivity)MainActivity.mContext).infoWindow.open(((MainActivity)MainActivity.mContext).selectedMarker);


        if(((MainActivity)MainActivity.mContext).srlRefresh.isRefreshing()) {
            ((MainActivity)MainActivity.mContext).srlRefresh.setRefreshing(false);
            Snackbar.make(((MainActivity)MainActivity.mContext).findViewById(R.id.clMain),
                    "재난문자 수신 완료.", Snackbar.LENGTH_SHORT).setGestureInsetBottomIgnored(true).show();
        }

        Log.d("customCheck", "onPostExecute() 완료");
    }

    public void requestApi(String msgNumber) {
        boolean Create_date_Flag = false, Location_name_Flag = false, Msg_Flag = false;
        String create_date = null, location_name = null, msg = null;

        // 신규 수신시, 기존 msgInfo를 비움.
//        msgInfo.clear();

        try{
            Log.d("customCheck", "재난문자 발령현황 API 파싱 시작");

            URL url = new URL("http://apis.data.go.kr/1741000/DisasterMsg3/getDisasterMsg1List?ServiceKey=" + getApiKey() +
                    "&type=xml" +
                    "&pageNo=1" +
                    "&numOfRows=" + msgNumber +
                    "&flag=Y"); // XML을 제공할 API의 주소

            Log.d("customCheck", "요청 URL : " + url);

            XmlPullParserFactory parserCreator = XmlPullParserFactory.newInstance();
            parserCreator.setNamespaceAware(true);
            XmlPullParser parser = parserCreator.newPullParser();
            BufferedInputStream bis = new BufferedInputStream(url.openStream());

            parser.setInput(bis, "utf-8");

            int parserEvent = parser.getEventType();

            while (parserEvent != XmlPullParser.END_DOCUMENT){
                switch(parserEvent){
                    case XmlPullParser.START_TAG://parser가 시작 태그를 만나면 실행
                        if(parser.getName().equals("create_date")){ //생성 날짜 태그를 만나면 활성화
                            Create_date_Flag = true;
                        }
                        if(parser.getName().equals("location_name")){ //지역 이름 태그를 만나면 활성화
                            Location_name_Flag = true;
                        }
                        if(parser.getName().equals("msg")){ //메시지 내용 태그를 만나면 활성화
                            Msg_Flag = true;
                        }
                        break;

                    case XmlPullParser.TEXT:// parser가 내용에 접근했을때
                        if(Create_date_Flag){ // Create_date_Flag이 true일 때 태그의 내용을 저장.
                            create_date = parser.getText();
                            Create_date_Flag = false;
                        }
                        if(Location_name_Flag){ // Location_name_Flag이 true일 때 태그의 내용을 저장.
                            location_name = parser.getText();
                            Location_name_Flag = false;
                        }
                        if(Msg_Flag){ // Msg_Flag이 true일 때 태그의 내용을 저장.
                            msg = parser.getText();
                            Msg_Flag = false;
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        if(parser.getName().equals("row")){
                            // MsgInfo 객체를 만들고, 하나의 메시지를 파싱한 결과를 종합한 후 해당 MsgInfo를 msgInfo 어레이 리스트에 추가
                            MsgInfo entity = new MsgInfo();
                            entity.setCreate_date(create_date);
                            entity.setLocation_name(location_name);
                            entity.setMsg(msg);
                            msgInfo.add(entity);
                        }
                        break;
                }
                parserEvent = parser.next();
            }
            Log.d("customCheck", "재난문자 수신현황 API 파싱 완료");
        } catch(Exception e){
            e.printStackTrace();
            Log.d("customCheck", "재난문자 수신현황 API 파싱 실패");


            Handler mHandler = new Handler(Looper.getMainLooper());
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
//                    Toast.makeText(((MainActivity)MainActivity.mContext),"재난문자 수신에 실패했습니다. 나중에 다시 시도해주세요.", Toast.LENGTH_LONG).show();
                    Snackbar.make(((MainActivity)MainActivity.mContext).findViewById(R.id.clMain),
                            "재난문자 수신에 실패했습니다. 나중에 다시 시도해주세요.", Snackbar.LENGTH_LONG).setGestureInsetBottomIgnored(true).show();
                }
            }, 0);
        }
    }

    public int[] getMsgCount(ArrayList<LocalInfo> localInfos) {
        // 수신된 msgInfo를 반복문을 통해 매개변수로 전달받은 지역이름 배열과 비교하여 숫자 카운트.
        locationMsgCnt = new int[localInfos.size()];

        locationMsgCnt[0] = msgInfo.size();
        for(int i = 1; i < localInfos.size(); i++) {
            locationMsgCnt[i] = 0;
            for(MsgInfo entity : msgInfo) {
                if(entity.getLocation_name().contains(localInfos.get(i).getLocal_name())) {
                    locationMsgCnt[i]++;
                }
            }
        }
        // 수신된 전체 메시지의 지역별 카운트 갯수 반환
        return locationMsgCnt;
    }

    public ArrayList<MsgInfo> getMsgInfo() {
        ArrayList<MsgInfo> deepCopy = new ArrayList<>(msgInfo.size());
        Iterator<MsgInfo> iterator = msgInfo.iterator();
        while(iterator.hasNext()) {
            deepCopy.add(iterator.next().clone());
        }

        return deepCopy;
    }

    private String getApiKey() {
        return "API_KEY";
    }
}
