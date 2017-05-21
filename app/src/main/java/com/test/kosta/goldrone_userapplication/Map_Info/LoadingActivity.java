package com.test.kosta.goldrone_userapplication.Map_Info;

import android.app.Activity;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;

import com.test.kosta.goldrone_userapplication.CPR_Info.CPRActivity;
import com.test.kosta.goldrone_userapplication.Http.HttpSend;
import com.test.kosta.goldrone_userapplication.R;

/**
 * Created by kosta on 2016-07-19.
 */
public class LoadingActivity extends Activity {

    GPSInfo gpsInfo;
    int drone_id;
    ImageButton btnmap, btncpr;
    Thread th;
    Handler mHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.e("LoadingActivity","onCreate start");

        setContentView(R.layout.activity_loading);

        mHandler = new Handler();
        gpsInfo = new GPSInfo(getApplicationContext());

        // loading, cpr btn
        btnmap = (ImageButton) findViewById(R.id.mapbtn);
        btncpr = (ImageButton) findViewById(R.id.cprbtn);
        // loading, cpr end


        mHandler.post(new Runnable() {

            @Override
            public void run() {

                Location loc = gpsInfo.getLocation();

                if (loc != null) {
                    th = new Thread() {
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

            }

        });


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
                Intent i = new Intent(LoadingActivity.this, CPRActivity.class);
                i.putExtra("page", 1);
                // shared preference로 바꾼 후 바꾸기!!!
                startActivity(i);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                finish();
                //}

            }
        });

        //map,cpr btn Onclick end

    }

/*    public void goNextPage() {

        Toast.makeText(getApplicationContext(), "find Drone", Toast.LENGTH_SHORT).show();

        Log.e("goNextPage","print");
        Intent i = new Intent(LoadingActivity.this, MapActivity.class);
        i.putExtra("id", drone_id);
        startActivity(i);

    }*/

}
