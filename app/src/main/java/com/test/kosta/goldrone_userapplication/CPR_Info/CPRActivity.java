package com.test.kosta.goldrone_userapplication.CPR_Info;

/**
 * Created by gahyeon on 2016-07-10.
 */

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.test.kosta.goldrone_userapplication.Map_Info.LoadingActivity;
import com.test.kosta.goldrone_userapplication.Map_Info.MapActivity;
import com.test.kosta.goldrone_userapplication.R;

import java.io.IOException;

public class CPRActivity extends FragmentActivity {

    ImageButton btnmap, btncpr;
    ImageView titlebar;
    LinearLayout btn_layout;
    Button btn_auto;
    Button btn_media[] = new Button[2];
    MediaPlayer mp;
    ViewPager viewPager = null;
    int autoplay = 1;
    int page = 0;
    int num;
    com.test.kosta.goldrone_userapplication.AED_Info.LinePageIndicator indicator;

    boolean firstCreate;
    String prefix = "android.resource://com.test.kosta.goldrone_userapplication/";

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cpr);

        // Btn Create
        btn_layout = (LinearLayout) findViewById(R.id.button_layout);
        titlebar = (ImageView) findViewById(R.id.titlebar);
        btnmap = (ImageButton) findViewById(R.id.mapbtn);
        btncpr = (ImageButton) findViewById(R.id.cprbtn);
        btn_auto = (Button) findViewById(R.id.btn_auto);
        btn_media[0] = (Button) findViewById(R.id.btn_pause);
        btn_media[1] = (Button) findViewById(R.id.btn_stop);
        Typeface font = Typeface.createFromAsset(getAssets(), "ENSW721L.ttf");
        btn_auto.setTypeface(font);
        btn_media[0].setTypeface(font);
        btn_media[1].setTypeface(font);

        ButtonsetListener();
        // Btn Create end


        //Intent
        Intent intent = getIntent();
        num = intent.getIntExtra("page", 0);

       if(num == 3){ // main
          //  btnmap.setVisibility(View.GONE);
            //btncpr.setVisibility(View.GONE);
            btn_layout.setVisibility(View.GONE);
        }
        //Intent end

        //viewPager
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        CPRAdapter adapter = new CPRAdapter(getSupportFragmentManager());

        viewPager.setAdapter(adapter);
        //viePager End

        //indicator
        indicator = (com.test.kosta.goldrone_userapplication.AED_Info.LinePageIndicator) findViewById(R.id.indicator);
        indicator.setViewPager(viewPager);
        indicator.setStrokeWidth(7);

        //sharedpreferences
        SharedPreferences get = getSharedPreferences("num", MODE_PRIVATE);
        int tempnum = get.getInt("PageNum", -1);

        if (tempnum == -1) // sharepreferences가 없을 때
        {
            // default
        } else {
            viewPager.setCurrentItem(tempnum);
        }
        //sharedpreferences end


        //mediaPlayer
        mp = MediaPlayer.create(getApplicationContext(), R.raw.c_audio00);
        MediaPlayersetListener();
        //
    }


    public void mediaPlayer(int index) {

        try {
            switch (index) {
                case 0:
                    mp.reset();
                    mp.setDataSource(getApplicationContext(), Uri.parse(prefix + R.raw.c_audio00));
                    mp.prepare();
                    break;
                case 1:
                    mp.reset();
                    mp.setDataSource(getApplicationContext(), Uri.parse(prefix + R.raw.c_audio01));
                    mp.prepare();
                    break;
                case 2:
                    mp.reset();
                    mp.setDataSource(getApplicationContext(), Uri.parse(prefix + R.raw.c_audio02));
                    mp.prepare();
                    break;
                case 3:
                    mp.reset();
                    mp.setDataSource(getApplicationContext(), Uri.parse(prefix + R.raw.c_audio03));
                    mp.prepare();
                    break;
                case 4:
                    mp.reset();
                    mp.setDataSource(getApplicationContext(), Uri.parse(prefix + R.raw.c_audio04));
                    mp.prepare();
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void MediaPlayersetListener() {

        mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {

                if (autoplay == 0) {
                    btn_media[0].setText("PLAY");
                } else if (autoplay == 1) {
                    page = viewPager.getCurrentItem();
                    page = (page == 4 ? 0 : page + 1);
                    viewPager.setCurrentItem(page);
                }

            }
        });

        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                //p = viewPager.getCurrentItem();
                Log.e("OnPageChangeListener", "" + position);
                mp.stop();
                mediaPlayer(position);

                if (mp != null)
                    btn_media[0].setText("PAUSE");
                mp.start();

            }

            @Override
            public void onPageSelected(int position) {
                indicator.setCurrentItem(viewPager.getCurrentItem());
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }

    @Override
    protected void onPause() {
        super.onPause();
        mp.pause();
        btn_media[0].setText("PLAY");
    }


    public void ButtonsetListener() {

                        btn_auto.setOnClickListener(new OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (autoplay == 1) { //autoplay == false
                                    btn_auto.setTextColor(Color.parseColor("#BBBBBB")); // <- check!!!!! 색 바꾸기
                                    autoplay = 0;
                                } else { //autoplay == true
                                    btn_auto.setTextColor(Color.parseColor("#EE4D39"));
                                    autoplay = 1;
                                    if (!mp.isPlaying())
                                        mp.start();
                                }
                            }
                        });

                        btn_media[0].setOnClickListener(new OnClickListener() { // play, pause btn
                            @Override
                            public void onClick(View v) {
                                if (mp.isPlaying()) {
                                    mp.pause();
                    Toast.makeText(CPRActivity.this, "PAUSE", Toast.LENGTH_SHORT).show();
                    btn_media[0].setText("PLAY");
                } else {
                    mp.start();
                    Toast.makeText(CPRActivity.this, "PLAY", Toast.LENGTH_SHORT).show();
                    btn_media[0].setText("PAUSE");
                }
            }
        });

        btn_media[1].setOnClickListener(new OnClickListener() { // stop btn
            @Override
            public void onClick(View v) {
                if (mp != null) {

                    mp.stop();
                    Toast.makeText(CPRActivity.this, "STOP", Toast.LENGTH_SHORT).show();
                    btn_media[0].setText("PLAY");

                    try {
                        mp.prepare();
                    } catch (IOException ie) {
                        ie.printStackTrace();
                    }

                    mp.seekTo(0);
                }
            }
        });

        btnmap.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {

                SharedPreferences pref = getSharedPreferences("num", MODE_PRIVATE);
                SharedPreferences.Editor editor = pref.edit();
                editor.putInt("PageNum", viewPager.getCurrentItem());
                editor.commit();

                if (num == 1) {
                    Intent i = new Intent(CPRActivity.this, LoadingActivity.class);
                    startActivity(i);
                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                    finish();
                } else if (num == 2) {
                    Intent i = new Intent(CPRActivity.this, MapActivity.class);
                    startActivity(i);
                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                    finish();
                }

            }
        });

        btncpr.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                // set first page
                viewPager.setCurrentItem(0);
                //
            }
        });
    }
}