package com.test.kosta.goldrone_userapplication.Map_Info;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;

import java.util.List;
import java.util.Locale;

public class GPSInfo implements LocationListener {

    private Context mContext;
    boolean isGPSEnabled = false, isNetworkEnabled = false, isGetLocation = false;

    Location location;
    double lat;
    double lng;

    //최소 GPS 정보 업데이트 거리 1미터v
   // private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 1;
    // 최소 GPS 정보 업데이트 시간 30초
  //  private static final long MIN_TIME_BW_UPDATES = 1000 * 30 * 1;

    protected LocationManager locationManager;

    public GPSInfo(Context context){
        this.mContext = context;
        Log.e("GPSInfo","Constructor OK");
      //  getLocation();
    }

    public Location getLocation() {

        Log.e("GPSInfo","getLocation OK");

        locationManager = (LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE);
        isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

        if(!isGPSEnabled && !isNetworkEnabled){ // 네트워크, GPS 안될 때
            Log.e("GPSInfo","Network GPS XXX");
            showSettingsAlert();
        }else{ //
            this.isGetLocation = true;

            if(isNetworkEnabled){
                Log.e("GPSInfo","Network O");

                //locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,
                   //    MIN_TIME_BW_UPDATES,MIN_DISTANCE_CHANGE_FOR_UPDATES,this);

                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,0,0,this);

                if(locationManager != null){
                    location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

                    if(location != null){
                        lat = location.getLatitude();
                        lng = location.getLongitude();

                    }
                }

            }

            if(isGPSEnabled){
                Log.e("GPSInfo","GPS O");

                if(location == null){

                    Log.e("GPSInfo","location null");

                    if(locationManager != null){
                        Log.e("GPSInfo","locationManager not null");
                        locationManager.requestLocationUpdates(
                                LocationManager.GPS_PROVIDER, 0, 0, this);

                        if (locationManager != null) {
                            Log.e("GPSInfo","locationManager not null");

                            location = locationManager
                                    .getLastKnownLocation(LocationManager.GPS_PROVIDER);
                            if (location != null) {
                                lat = location.getLatitude();
                                lng = location.getLongitude();
                            }
                        }

                    }
                }
            }

        }

        return location;

    }

    public void stopUsingGPS(){
        if(locationManager != null){
            locationManager.removeUpdates(GPSInfo.this);
        }
    }

    public double getLatitude(){
        if(location != null){
            lat = location.getLatitude();
        }
        return lat;
    }

    public double getLongitude(){
        if(location != null){
            lng = location.getLongitude();
        }
        return lng;
    }

    public String getAddress(double lat, double lng){

        String address = null;

        //위치정보를 활용하기 위한 구글 API 객체
        Geocoder geocoder = new Geocoder(mContext, Locale.getDefault());

        //주소 목록을 담기 위한 HashMap
        List<Address> list = null;

        try{
            list = geocoder.getFromLocation(lat, lng, 1);
        } catch(Exception e){
            e.printStackTrace();
        }

        if(list == null){
            Log.e("getAddress", "주소 데이터 얻기 실패");
            return null;
        }

        if(list.size() > 0){
            Address addr = list.get(0);
            address = addr.getCountryName() + " "
                    + addr.getLocality() + " "
                    + addr.getThoroughfare() + " "
                    + addr.getFeatureName();
        }

        return address;


    }

    public boolean isGetLocation(){
        return this.isGetLocation;
    }


    public void showSettingsAlert(){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(mContext);

        alertDialog.setTitle("GPS 사용유무셋팅");
        alertDialog.setMessage("GPS 셋팅이 되지 않았을수도 있습니다.\n 설정창으로 가시겠습니까?");

                // OK 를 누르게 되면 설정창으로 이동합니다.
                alertDialog.setPositiveButton("Settings",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                                mContext.startActivity(intent);
                            }
                        });
        // Cancle 하면 종료 합니다.
        alertDialog.setNegativeButton("Cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

        alertDialog.show();
    }

    public void onLocationChanged(Location location) {
        // TODO Auto-generated method stub

        Log.e("GPSInfo","onLocationChanged O");

        this.location = location;
        lat = location.getLatitude();
        lng = location.getLongitude();
    }

    public void onStatusChanged(String provider, int status, Bundle extras) {
        // TODO Auto-generated method stub

    }

    public void onProviderEnabled(String provider) {
        // TODO Auto-generated method stub

    }

    public void onProviderDisabled(String provider) {
        // TODO Auto-generated method stub

    }


}