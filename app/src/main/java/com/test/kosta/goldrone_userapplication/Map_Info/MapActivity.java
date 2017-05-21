package com.test.kosta.goldrone_userapplication.Map_Info;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.test.kosta.goldrone_userapplication.AED_Info.AEDActivity;
import com.test.kosta.goldrone_userapplication.CPR_Info.CPRActivity;
import com.test.kosta.goldrone_userapplication.DB.DataBaseHelper;
import com.test.kosta.goldrone_userapplication.DB.DroneGPS;
import com.test.kosta.goldrone_userapplication.DB.UserGPS;
import com.test.kosta.goldrone_userapplication.R;

import net.daum.mf.map.api.CameraUpdateFactory;
import net.daum.mf.map.api.MapPOIItem;
import net.daum.mf.map.api.MapPoint;
import net.daum.mf.map.api.MapPointBounds;
import net.daum.mf.map.api.MapPolyline;
import net.daum.mf.map.api.MapView;

import java.util.List;

public class MapActivity extends Activity implements MapView.MapViewEventListener, MapView.POIItemEventListener {


    MapView mapView;
    GPSInfo gpsInfo;

    int drone_num;
    // 드론 경로 마커 숫자

    DataBaseHelper mHelper;
    MapPolyline polyline;
    //boolean threadstop = false;

    ImageButton btnmap, btncpr;
    TextView d_id, d_add, d_cur, d_target, d_time, d_dist;
    double lat, lng;
    String address;
    String add_call;

    Thread th;

    boolean check = false;
    boolean endpin = false, midpin = false;

    List<DroneGPS> drones;
    MapPOIItem m_marker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        // map, cpr btn
        btnmap = (ImageButton) findViewById(R.id.mapbtn);
        btncpr = (ImageButton) findViewById(R.id.cprbtn);
        // map, cpr end


        gpsInfo = new GPSInfo(getApplicationContext());

        //

        //


        //map,cpr btn setOnclickListener

        mHelper = new DataBaseHelper(getApplicationContext());

        mapView = new MapView(this);
        mapView.setDaumMapApiKey("eeaa7ec9af9959deefc98ebe47ff11ec");

        RelativeLayout container = (RelativeLayout) findViewById(R.id.map_view);
        container.addView(mapView);

        mapView.setMapViewEventListener(this);
        mapView.setPOIItemEventListener(this);

        // 줌 인, 줌 아웃
        mapView.zoomIn(true);
        mapView.zoomOut(true);

        // 드론 정보 텍스트뷰
        d_id = (TextView) findViewById(R.id.drone_id);
        d_add = (TextView) findViewById(R.id.drone_add);
        d_cur = (TextView) findViewById(R.id.drone_cur);
        d_target = (TextView) findViewById(R.id.drone_target);
        d_time = (TextView) findViewById(R.id.drone_time);
        d_dist = (TextView) findViewById(R.id.drone_dist);

        //
        Intent start = getIntent();
        int drone_id = start.getIntExtra("id", 0);
        d_id.setText("[ " + drone_id + "번 골드론이 출동 중 입니다 ]");

        th = new Thread() {

            @Override
            public void run() {
                super.run();

                drone_num = 0;

                while (true) {

                    drones = mHelper.getDroneGpsList();

                    //Log.e("drones", "" + drones.size());

                    if (drones.size() == 0) {

                    } else {

                        if (drones.size() == 1) {
/*
                            mapView.setMapCenterPointAndZoomLevel(MapPoint.mapPointWithGeoCoord(
                                    (lat + drones.get(0).getLat())/2,(lng + drones.get(0).getLon())/2), 3, true);*/


                            MapPointBounds mapPointBounds = new MapPointBounds(
                                    MapPoint.mapPointWithGeoCoord(lat, lng),
                                    MapPoint.mapPointWithGeoCoord(drones.get(0).getLat(), drones.get(0).getLon())
                            );

                            mapView.moveCamera(CameraUpdateFactory.newMapPointBounds(mapPointBounds, 100));

                            if (!endpin) {
                                MapPOIItem s_marker = new MapPOIItem();
                                s_marker.setItemName("Start Point");
                                s_marker.setMapPoint(MapPoint.mapPointWithGeoCoord(drones.get(0).getLat(), drones.get(0).getLon()));
                                s_marker.setMarkerType(MapPOIItem.MarkerType.BluePin);
                                mapView.addPOIItem(s_marker);
                                endpin = true;
                            }

                            if (!check) {

                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {

                                        address = gpsInfo.getAddress(drones.get(0).getLat(), drones.get(0).getLon());

                                        if (address == null) {
                                            address = "주소 정보가 없습니다. ( lat : " + lat + ", lon : " + lng + ")";
                                        }

                                        //d_target.append(add_call);
                                        d_add.append(address);
                                        d_cur.append(address);

                                        // 예상소요시간 계산
                                        double dist = calDistance(lat, lng, drones.get(0).getLat(), drones.get(0).getLon()); // 호출위치와 드론최초위치 사이 거리(m)
                                        double velo = 20; // 골드론의 속도 20m/s(72km/h)로 설정
                                        double time = Math.round(dist / velo);
                                        int time_m = (int) time / 60;
                                        int time_s = (int) time % 60;
                                        String lead_time = "" + time_m + "분 " + time_s + "초";
                                        d_time.setText("소요시간 : " + lead_time);
                                        d_dist.setText("/    남은거리 : " + Math.round(dist) + "m");
                                        //

                                        check = true;

                                    }
                                });


                            }

                            drone_num = 0;


                        } else if (drones.size() > 1) {

                            Log.e("dronesize", "" + drones.size());


                            //
                            for (int i = drone_num; i < drones.size(); i++) {
                                polyline.addPoint(MapPoint.mapPointWithGeoCoord(drones.get(i).getLat(), drones.get(i).getLon()));
                            }
                            mapView.addPolyline(polyline);
                            //polyline

                            drone_num = drones.size() - 1;

                            if (Math.abs(lat - drones.get(drones.size() - 1).getLat()) < 0.0005 &&
                                    Math.abs(lng - drones.get(drones.size() - 1).getLon()) < 0.0005) {
                                // 드론 도착(최근위치)
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(getApplicationContext(), "드론이 도착하였습니다. AED안내로 이동합니다.", Toast.LENGTH_SHORT).show();
                                    }
                                });
                                goNextPage();
                                currentThread().interrupt();
                            }

                            if (!endpin) {
                                MapPOIItem s_marker = new MapPOIItem();
                                s_marker.setItemName("Start Point");
                                s_marker.setMapPoint(MapPoint.mapPointWithGeoCoord(drones.get(0).getLat(), drones.get(0).getLon()));
                                s_marker.setMarkerType(MapPOIItem.MarkerType.BluePin);
                                mapView.addPOIItem(s_marker);
                                endpin = true;
                            }

                            if (!midpin) {
                                m_marker = new MapPOIItem();
                                m_marker.setItemName("Mid Point");
                                m_marker.setMapPoint(MapPoint.mapPointWithGeoCoord(drones.get(drones.size() - 1).getLat(), drones.get(drones.size() - 1).getLon()));
                                m_marker.setMarkerType(MapPOIItem.MarkerType.YellowPin);
                                mapView.addPOIItem(m_marker);
                                midpin = true;
                            } else {
                                m_marker.setMapPoint(MapPoint.mapPointWithGeoCoord(drones.get(drones.size() - 1).getLat(), drones.get(drones.size() - 1).getLon()));
                            }

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    int idx = drones.size() - 1;

                                    address = gpsInfo.getAddress(drones.get(idx).getLat(), drones.get(idx).getLon());
                                    if (address == null) {
                                        address = "주소 정보가 없습니다. ( lat : " + lat + ", lon : " + lng + ")";
                                    }
                                    d_cur.setText("  현재드론위치 : " + address);

                                    // 예상소요시간 계산
                                    double dist = calDistance(lat, lng, drones.get(idx).getLat(), drones.get(idx).getLon()); // 호출위치와 드론최초위치 사이 거리(m)
                                    double velo = 20; // 골드론의 속도 20m/s(72km/h)로 설정
                                    double time = Math.round(dist / velo); // 예상시간
                                    int time_m = (int) time / 60; // 분
                                    int time_s = (int) time % 60; // 초
                                    String lead_time = "" + time_m + "분 " + time_s + "초";
                                    d_time.setText("소요시간 : " + lead_time);
                                    d_dist.setText("/    남은거리 : " + Math.round(dist));
                                    //
                                }
                            });

                        }

                        try {
                            Thread.sleep(10000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }

            }

        };

        btnmap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //
            }
        });

        btncpr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // set first page
                th.interrupt();

                // if(th.isInterrupted()){
                Intent i = new Intent(MapActivity.this, CPRActivity.class);
                // shared preference로 바꾼 후 바꾸기!!!
                i.putExtra("page", 2);
                startActivity(i);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                finish();
                //}

            }
        });

        //map,cpr btn Onclick end

    }

    public void goNextPage() {
        Intent aed = new Intent(MapActivity.this, AEDActivity.class);
        startActivity(aed);
        finish();
    }

    @Override
    public void onMapViewInitialized(MapView view) {

        UserGPS user = mHelper.getUserGPS();

        // 사용자 위치
        lat = user.getLat();
        lng = user.getLon();

        add_call = gpsInfo.getAddress(lat, lng);

        if (address == null) {
            address = "주소 정보가 없습니다. ( lat : " + lat + ", lon : " + lng + ")";
        }

        d_target.append(add_call);


        // 중심점 변경 + 줌 레벨 변경
        mapView.setMapCenterPointAndZoomLevel(MapPoint.mapPointWithGeoCoord(user.getLat(), user.getLon()), 3, true);

        MapPOIItem e_marker = new MapPOIItem();
        e_marker.setItemName("End Point");
        e_marker.setMapPoint(MapPoint.mapPointWithGeoCoord(lat, lng));
        e_marker.setMarkerType(MapPOIItem.MarkerType.RedPin);
        mapView.addPOIItem(e_marker);

        //

        polyline = new MapPolyline();
        polyline.setTag(1000);
        polyline.setLineColor(Color.argb(128, 255, 51, 0));

        th.start();
    }

    @Override
    public void onMapViewCenterPointMoved(MapView mapView, MapPoint mapPoint) {
    }

    @Override
    public void onMapViewZoomLevelChanged(MapView mapView, int i) {
    }

    @Override
    public void onMapViewSingleTapped(MapView mapView, MapPoint mapPoint) {
    }

    @Override
    public void onMapViewDoubleTapped(MapView mapView, MapPoint mapPoint) {
    }

    @Override
    public void onMapViewLongPressed(MapView mapView, MapPoint mapPoint) {
    }

    @Override
    public void onMapViewDragStarted(MapView mapView, MapPoint mapPoint) {
    }

    @Override
    public void onMapViewDragEnded(MapView mapView, MapPoint mapPoint) {
    }

    @Override
    public void onMapViewMoveFinished(MapView mapView, MapPoint mapPoint) {
    }

    @Override
    public void onPOIItemSelected(MapView mapView, MapPOIItem mapPOIItem) {

    }

    @Override
    public void onCalloutBalloonOfPOIItemTouched(MapView mapView, MapPOIItem mapPOIItem) {

    }

    @Override
    public void onCalloutBalloonOfPOIItemTouched(MapView mapView, MapPOIItem mapPOIItem, MapPOIItem.CalloutBalloonButtonType calloutBalloonButtonType) {

    }

    @Override
    public void onDraggablePOIItemMoved(MapView mapView, MapPOIItem mapPOIItem, MapPoint mapPoint) {

    }

    public double calDistance(double lat1, double lon1, double lat2, double lon2) {

        double theta, dist;
        theta = lon1 - lon2;
        dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2)) + Math.cos(deg2rad(lat1))
                * Math.cos(deg2rad(lat2)) * Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = rad2deg(dist);

        dist = dist * 60 * 1.1515;
        dist = dist * 1.609344;    // 단위 mile 에서 km 변환.
        dist = dist * 1000.0;      // 단위  km 에서 m 로 변환

        return dist;
    }

    // 주어진 도(degree) 값을 라디언으로 변환
    private double deg2rad(double deg) {
        return (double) (deg * Math.PI / (double) 180d);
    }

    // 주어진 라디언(radian) 값을 도(degree) 값으로 변환
    private double rad2deg(double rad) {
        return (double) (rad * (double) 180d / Math.PI);
    }

}
