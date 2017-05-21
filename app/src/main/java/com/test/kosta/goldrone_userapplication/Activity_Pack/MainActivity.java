package com.test.kosta.goldrone_userapplication.Activity_Pack;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.test.kosta.goldrone_userapplication.AED_Info.AEDActivity;
import com.test.kosta.goldrone_userapplication.CPR_Info.CPRActivity;
import com.test.kosta.goldrone_userapplication.Http.HttpReceive;
import com.test.kosta.goldrone_userapplication.Http.HttpSend;
import com.test.kosta.goldrone_userapplication.Map_Info.GPSInfo;
import com.test.kosta.goldrone_userapplication.Map_Info.LoadingActivity;
import com.test.kosta.goldrone_userapplication.Map_Info.MapActivity;
import com.test.kosta.goldrone_userapplication.R;

public class MainActivity extends Activity {

    ImageButton emg_btn;

    ImageButton btn_119,btn_aed,btn_cpr;

    String address;
    int drone_id;
    Context mContext;
    HttpReceive httpReceive;

    Handler mHandler;
    GPSInfo gpsInfo;
    SmsManager smsManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mContext = getApplicationContext();
        mHandler = new Handler();
        smsManager = SmsManager.getDefault();

        Intent i = getIntent();
        address = i.getStringExtra("address");

        //drone_id = 1;

        emg_btn = (ImageButton) findViewById(R.id.emergenyButton);

        btn_119 = (ImageButton) findViewById(R.id.btn_119);
        btn_aed = (ImageButton) findViewById(R.id.btn_aed);
        btn_cpr = (ImageButton) findViewById(R.id.btn_cpr);

        downBtnsetListener();

        emg_btn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                //sharedpreferences
                SharedPreferences get = getSharedPreferences("drone", MODE_PRIVATE);
                int tempnum = get.getInt("droneNum", -1);

                if (tempnum == -1) // sharepreferences가 없을 때
                {
                    // default

                    //
                    String msg = "[긴급구조요청] '" + address + "' 심방세동 환자 발생";
                    smsManager.sendTextMessage("010-8805-4317", null, msg, null, null);
                    Toast.makeText(mContext, "문자메세지가 전송되었습니다", Toast.LENGTH_LONG).show();
                    //

                    mHandler.post(new Runnable() {

                        @Override
                        public void run() {

                            gpsInfo = new GPSInfo(getApplicationContext());

                            if (gpsInfo.getLocation() != null) {

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
                                                //
                                                SharedPreferences pref = getSharedPreferences("drone", MODE_PRIVATE);
                                                SharedPreferences.Editor editor = pref.edit();
                                                editor.putInt("droneNum", drone_id);
                                                editor.commit();
                                                //
                                                break;
                                            }
                                        }

                                    }
                                };

                                th.start();

                                try {
                                    th.join();

                                    if(th.isAlive()){
                                        //
                                        Log.e("Thread","end X");
                                    }else{
                                        goNextPage();
                                    }

                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }

                            } else {
                                Log.e("mHandler", "GPSInfo null");
                            }
                        }

                    });


                } else {

                    drone_id = tempnum;
                    goNextPage();

                }
                //sharedpreferences end

                Log.e("drone_id click",drone_id+"");

            }
        });


    }

    public void downBtnsetListener(){

        btn_119.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 119 통화
                try {
                    String contact_number="01088054317";
                    Intent callIntent = new Intent(Intent.ACTION_CALL);
                    callIntent.setData(Uri.parse("tel:" + contact_number));
                    startActivity(callIntent);
                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                } catch (Exception e) {
                    // no activity to handle intent. show error dialog/toast whatever
                }

            }
        });

        btn_aed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in = new Intent(MainActivity.this, AEDActivity.class);
                startActivity(in);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            }
        });

        btn_cpr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in = new Intent(MainActivity.this, CPRActivity.class);
                in.putExtra("page",3);
                startActivity(in);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            }
        });

    }

    public void goNextPage(){

        if (drone_id == -1) {
            // 대기 페이지 !!!!
            Intent intent = new Intent(MainActivity.this, LoadingActivity.class);
            startActivity(intent);
           // finish();

        }else if(drone_id != -1){

            Log.e("else if",""+drone_id);
            httpReceive = new HttpReceive(getApplicationContext(), drone_id);
            httpReceive.ReceiveInfo();

            Intent start = new Intent(MainActivity.this, MapActivity.class);
            start.putExtra("id", drone_id);
            //start.putExtra("address", address);
            startActivity(start);
        }

    }
}