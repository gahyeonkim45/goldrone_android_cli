package com.test.kosta.goldrone_userapplication.Activity_Pack;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.test.kosta.goldrone_userapplication.DB.DataBaseHelper;
import com.test.kosta.goldrone_userapplication.DB.UserGPS;
import com.test.kosta.goldrone_userapplication.Map_Info.GPSInfo;
import com.test.kosta.goldrone_userapplication.R;

/**
 * Created by gahyeon on 2016-07-10.
 */
public class SplashActivity extends Activity {

    GPSInfo gpsInfo;

    // int drone_id;
    double lat, lng;
    String address;

    /**
     * 처음 액티비티가 생성될때 불려진다.
     */
    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setContentView(R.layout.activity_splash);

        Log.e("Splash Activity", "start ! ");

        DisplayMetrics metrics = new DisplayMetrics();
        WindowManager windowManager = (WindowManager) getApplicationContext()
                .getSystemService(Context.WINDOW_SERVICE);
        windowManager.getDefaultDisplay().getMetrics(metrics);

        ImageView img = (ImageView) findViewById(R.id.splashimg);
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) img.getLayoutParams();
        params.width = metrics.widthPixels;
        params.height = metrics.heightPixels;
        img.setLayoutParams(params);

        SharedPreferences.Editor get = getSharedPreferences("num", MODE_PRIVATE).edit();
        get.remove("PageNum");
        get.commit();

        SharedPreferences.Editor getdrone = getSharedPreferences("drone", MODE_PRIVATE).edit();
        getdrone.remove("droneNum");
        getdrone.commit();

        FetchDataTask fetchDataTask = new FetchDataTask();
        fetchDataTask.execute();

    }

    public class FetchDataTask extends AsyncTask<Void, Void, Void> {

        DataBaseHelper mHelper;
        Handler mHandler = new Handler();
        boolean endcheck = false;

        @Override
        protected Void doInBackground(Void... voids) {

            //
            Thread th = new Thread() {
                @Override
                public void run() {
                    super.run();

                    try {
                        currentThread().sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            };

            th.start();

            try {
                th.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            //

            mHelper = new DataBaseHelper(getApplicationContext());
            gpsInfo = new GPSInfo(getApplicationContext());

            //
            //mHelper.deleteDroneAll();
            //mHelper.deleteUserAll();
            //


            mHandler.post(new Runnable() {

                @Override
                public void run() {

                    mHelper.deleteAll();
                    gpsInfo.getLocation();

                    if (gpsInfo != null) {
                        Log.e("User Lat,Lng", gpsInfo.getLatitude() + " , " + gpsInfo.getLongitude());

                        lat = gpsInfo.getLatitude();
                        lng = gpsInfo.getLongitude();

                        mHelper.addUserGPS(new UserGPS(lat, lng));

                        endcheck = true;
                    }

                    address = gpsInfo.getAddress(lat, lng);
                    if (address == null) {
                        address = "주소 정보가 없습니다. ( lat : " + lat + ", lon : " + lng + ")";
                    }


                }

            });


            while(!endcheck){
                // mHandler 종료를 기다림!
            }

            return null;
        }


        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            // Log.e("drone_id,address", "" + drone_id + ", " + address);

            Intent i = new Intent(SplashActivity.this, MainActivity.class);
            // i.putExtra("id", drone_id);
            i.putExtra("address", address);
            startActivity(i);
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            finish();

        }


    }

    /***
     *
     *  if (loc != null) {
     Thread th = new Thread() {
     public void run() {
     // gps 정보로 가까운 드론 조회

     while (true) {
     drone_id = HttpSend.SendInfo(gpsInfo.getLocation());

     Log.e("mHandler", "" + drone_id);


     if (drone_id == 0) {
     Log.e("drone_id", "null");

     try {
     Thread.sleep(5000);
     } catch (InterruptedException e) {
     e.printStackTrace();
     }

     } else {
     Log.e("drone_id", "" + drone_id);
     break;
     }
     }

     }
     };

     th.start();

     try {
     th.join();
     } catch (InterruptedException e) {
     e.printStackTrace();
     }

     } else {
     Log.e("mHandler", "GPSInfo null");
     }
     */

}