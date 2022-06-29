package com.example.disaster_message_notificator;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.snackbar.Snackbar;
import com.naver.maps.geometry.LatLng;
import com.naver.maps.geometry.LatLngBounds;
import com.naver.maps.map.CameraPosition;
import com.naver.maps.map.MapView;
import com.naver.maps.map.NaverMap;
import com.naver.maps.map.OnMapReadyCallback;
import com.naver.maps.map.overlay.InfoWindow;
import com.naver.maps.map.overlay.Marker;
import com.naver.maps.map.overlay.Overlay;
import com.naver.maps.map.util.MarkerIcons;
import com.naver.maps.map.widget.ZoomControlView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback, SwipeRefreshLayout.OnRefreshListener {
    public static Context mContext;
    private static NaverMap naverMap;
    private MapView mapView;
    private List<Marker> markers;
    protected Marker selectedMarker;
    protected InfoWindow infoWindow;

    private MsgInfoAdapter msgInfoAdapter;
    private ListView lvMsgList;
    private long backKeyPressedTime;
    protected MainModel mainModel;
    protected ArrayList<MsgInfo> msgInfo;
    protected SwipeRefreshLayout srlRefresh;
    protected ProgressDialog customProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        StrictMode.enableDefaults();
        init(savedInstanceState);
        setTitle("재난문자 알림이");
//        getSupportActionBar().setSubtitle("가성비");


        reaction();         // 사용자 입력 반응

        mainModel = new MainModel();

        customProgressDialog = new ProgressDialog(this);
        customProgressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        customProgressDialog.show();
//        srlRefresh.setRefreshing(true);
    }

    public void init(Bundle savedInstanceState) {
        mContext = this;
        srlRefresh  = (SwipeRefreshLayout)findViewById(R.id.srlRefresh);
        srlRefresh.setColorSchemeResources(R.color.colorPrimary, R.color.colorPrimaryDark, R.color.colorAccent);
        srlRefresh.setOnRefreshListener(this);

        lvMsgList = (ListView) findViewById(R.id.lvMsgList);
        backKeyPressedTime = 0;
        mapView = (MapView) findViewById(R.id.map);
        markers = new ArrayList<>();
        infoWindow = new InfoWindow();

        msgInfo = new ArrayList<MsgInfo>();

        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);
    }

    @Override
    public void onRefresh() {
        Log.d("customCheck", "당겨서 새로고침 시작");
        srlRefresh.setRefreshing(true);
        doRefresh();
        Log.d("customCheck", "당겨서 새로고침 완료");
    }

    @Override
    public void onBackPressed() {
        // 기존의 뒤로가기 버튼의 기능 제거
        // super.onBackPressed();

        // 2000 milliseconds = 2 seconds
        if (System.currentTimeMillis() > backKeyPressedTime + 2000) {
            backKeyPressedTime = System.currentTimeMillis();
//            Toast.makeText(this, "\'뒤로\' 버튼을 한번 더 누르시면 종료됩니다.", Toast.LENGTH_SHORT).show();
            Snackbar.make(findViewById(R.id.clMain), "앱을 종료하려면 \'뒤로가기\' 버튼을 한번 더 눌러주세요.", Snackbar.LENGTH_SHORT).setGestureInsetBottomIgnored(true).show();
            return;
        }

        // 2초 이내에 뒤로가기 버튼을 한번 더 클릭시 finish()(앱 종료)
        if (System.currentTimeMillis() <= backKeyPressedTime + 2000) {
            finish();
        }
    }

    @Override
    public void onMapReady(@NonNull NaverMap naverMap) {
        this.naverMap = naverMap;

        naverMap.setMapType(NaverMap.MapType.Navi);
        naverMap.setLayerGroupEnabled(NaverMap.LAYER_GROUP_BUILDING, false);
        naverMap.setLayerGroupEnabled(NaverMap.LAYER_GROUP_TRAFFIC, false);
        naverMap.getUiSettings().setScaleBarEnabled(false);
        naverMap.getUiSettings().setZoomControlEnabled(false);

        ZoomControlView zoomControlView = findViewById(R.id.zoom);
        zoomControlView.setMap(naverMap);

        CameraPosition cameraPosition = new CameraPosition(new LatLng(35.90, 127.76),5.0);
        naverMap.setCameraPosition(cameraPosition);

        naverMap.setMinZoom(5.0);
        naverMap.setMaxZoom(11.0);
        naverMap.setExtent(new LatLngBounds(new LatLng(31.43, 122.37), new LatLng(44.35, 132)));

        setMarker();        //마커 설정
        mapEvent();         //이벤트 설정

    }

    public void setMarker() {
        //마커 생성

        for(int i = 0; i < mainModel.setting.getLocalInfos().size(); i++) {
            Marker marker = new Marker();

            marker.setCaptionText(mainModel.getSettingLocalInfo().get(i).getLocal_name());
            marker.setPosition(new LatLng(
                    Double.parseDouble(mainModel.getSettingLocalInfo().get(i).getLatitude()),
                    Double.parseDouble(mainModel.getSettingLocalInfo().get(i).getLongitude())));

            //(전국)-(광역시,도)-(시,구,군) 별로 태그값 설정
            if(i == 0) {
                marker.setTag(0);
            } else if((0 < i) && (i <= 17)) {
                marker.setTag(1);
            } else {
                marker.setTag(2);
            }

            markers.add(marker);
        }

        Log.d("customCheck", "마커 생성 성공");

        // 마커 색상 설정
        mainModel.countPerLocation();

        // 마커 표시
        for(Marker marker : markers) {
            marker.setMap(naverMap);
        }

        // 초기 선택 마커 설정
        selectedMarker = markers.get(0);
//        infoWindow.open(selectedMarker);
    }

    public void setMarkerColor(int[] locMsgCnt) {
        //선택한 마커에 해당하는 지역에 수신된 재난문자 숫자에 따라 색상 설정
        int index = 0;
        for(Marker marker : markers) {
            if(locMsgCnt[index] == 0) {
                marker.setIcon(MarkerIcons.GRAY);
            } else if(locMsgCnt[index] < 5) {
                marker.setIcon(MarkerIcons.BLUE);
            } else if(locMsgCnt[index] < 10) {
                marker.setIcon(MarkerIcons.LIGHTBLUE);
            } else if(locMsgCnt[index] < 20) {
                marker.setIcon(MarkerIcons.GREEN);
            } else if(locMsgCnt[index] < 40) {
                marker.setIcon(MarkerIcons.YELLOW);
            } else {
                marker.setIcon(MarkerIcons.RED);
            }
        index++;
        }
    }

    public void mapEvent() {
        // 지도 이동, 마커 선택에 따른 이벤트 처리
        naverMap.addOnCameraChangeListener(new NaverMap.OnCameraChangeListener() {
            @Override
            public void onCameraChange(int i, boolean b) {
                double zoomLevel = naverMap.getCameraPosition().zoom;
                if(7 < zoomLevel) {
                    for(Marker marker : markers) {
                        if(marker.getTag().equals(2)) {
                            marker.setMap(naverMap);
                        } else {
                            marker.setMap(null);
                        }
                    }
                } else if((5 < zoomLevel) && (zoomLevel <= 7)) {
                    for(Marker marker : markers) {
                        if(marker.getTag().equals(1)) {
                            marker.setMap(naverMap);
                        } else {
                            marker.setMap(null);
                        }
                    }
                } else if(zoomLevel <= 5) {
                    for(Marker marker : markers) {
                        if(marker.getTag().equals(0)) {
                            marker.setMap(naverMap);
                        } else {
                            marker.setMap(null);
                        }
                    }
                }
                infoWindow.open(selectedMarker);
            }
        });

        for(final Marker marker : markers) {
            marker.setOnClickListener(new Overlay.OnClickListener() {
                @Override
                public boolean onClick(@NonNull Overlay overlay) {
                    if(!marker.equals(selectedMarker)) {
                        mainModel.selectLocation(marker.getCaptionText());
                        mainModel.processingMsg();
                        getMsg();

                        selectedMarker = marker;

                        // 정보 창을 열기 위해 객체 생성 후 어댑터 객체 지정
                        infoWindow.setAdapter(new InfoWindow.DefaultTextAdapter(getApplicationContext()) {
                            @NonNull
                            @Override
                            public CharSequence getText(@NonNull InfoWindow infoWindow) {
                                return "재난문자 : " + msgInfo.size() + "건";
                            }
                        });

                        if (selectedMarker.getInfoWindow() == null) {
                            // 현재 마커에 정보 창이 열려있지 않을 경우 엶
                            infoWindow.open(selectedMarker);
                        } else {
                            // 이미 현재 마커에 정보 창이 열려있을 경우 닫음
//                        infoWindow.close();
                        }

//                    Toast.makeText(getApplicationContext(), "마커선택 : " + selectedMarker.getCaptionText(), Toast.LENGTH_SHORT).show();
//                        Snackbar.make(findViewById(R.id.clMain), selectedMarker.getCaptionText() + " 선택.", Snackbar.LENGTH_SHORT).setGestureInsetBottomIgnored(true).show();
                    }
                    return false;
                }
            });
        }
    }

    public void doRefresh() {
        mainModel.refreshDisasterApi();
    }

    public void getMsg() {
        // 메시지 표시(메시지 가져오기)
        msgInfo = mainModel.showMsg();

        ArrayList<MsgInfo> sampleMsgInfos = new ArrayList<MsgInfo>();

        for(int i = 0; i < msgInfo.size(); i++) {
            MsgInfo tempMsgInfo = msgInfo.get(i);
            sampleMsgInfos.add(tempMsgInfo.clone());

//            String sampleMsg = new String();
//            String sampleCreateDate = new String();
//
//            if(msgInfo.get(i).getLocation_name().length() >= 12) {
//                sampleCreateDate = msgInfo.get(i).getLocation_name().substring(0, 11) + "...";
//            } else {
//                sampleCreateDate = msgInfo.get(i).getLocation_name();
//            }
//
//            if(msgInfo.get(i).getMsg().length() >= 40) {
//                sampleMsg = msgInfo.get(i).getMsg().substring(0, 39) + "...";
//            } else {
//                sampleCreateDate = msgInfo.get(i).getMsg();
//            }
//
//            sampleMsgInfos.get(i).setLocation_name(sampleCreateDate);
//            sampleMsgInfos.get(i).setMsg(sampleMsg);

//            sampleMsgInfos.get(i).setLocation_name(msgInfo.get(i).getLocation_name());
//            sampleMsgInfos.get(i).setMsg(msgInfo.get(i).getMsg());
        }

        msgInfoAdapter = new MsgInfoAdapter(this, sampleMsgInfos, lvMsgList);
        lvMsgList.setAdapter(msgInfoAdapter);
    }

    public void reaction() {
        lvMsgList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(), MsgActivity.class);
                intent.putExtra("CreateDate", msgInfo.get(position).getCreate_date());
                intent.putExtra("LocationName", msgInfo.get(position).getLocation_name());
                intent.putExtra("Msg", msgInfo.get(position).getMsg());
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
//        menu.add(0, 0, 0, "설정");
//        menu.add(0, 1, 0, "도움말");
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        super.onOptionsItemSelected(item);

        switch (item.getItemId()) {
            case R.id.btnSetting:
                Intent intent = new Intent(getApplicationContext(), SettingActivity.class);
                startActivity(intent);
                return true;
            case R.id.btnGuide:
                Intent guideIntent = new Intent(getApplicationContext(), GuideActivity.class);
                startActivity(guideIntent);
                return true;
        }
        return false;
    }
}