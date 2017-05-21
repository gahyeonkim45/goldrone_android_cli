package com.test.kosta.goldrone_userapplication.AED_Info;

/**
 * Created by gahyeon on 2016-07-10.
 */

import android.graphics.Color;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.test.kosta.goldrone_userapplication.R;

import java.io.IOException;

public class AEDActivity extends FragmentActivity {

    Button btn_auto;
    Button btn_media[] = new Button[2];
    MediaPlayer mp;
    ViewPager viewPager = null;
    int autoplay = 1;
    int page = 0;

    String prefix = "android.resource://com.test.kosta.goldrone_userapplication/";

    private ViewPager.OnPageChangeListener mOnPageChangeListener;

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aed);

        //viewPager
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        final AEDAdapter adapter = new AEDAdapter(getSupportFragmentManager());

        viewPager.setAdapter(adapter);

        //mediaPlayer
        mp = MediaPlayer.create(AEDActivity.this, R.raw.a_audio00);

        //indicator
        final LinePageIndicator indicator = (LinePageIndicator) findViewById(R.id.indicator);
        indicator.setViewPager(viewPager);
        indicator.setStrokeWidth(7);

        //button init
        btn_auto = (Button) findViewById(R.id.btn_auto);
        btn_media[0] = (Button) findViewById(R.id.btn_pause);
        btn_media[1] = (Button) findViewById(R.id.btn_stop);
        Typeface font = Typeface.createFromAsset(getAssets(), "ENSW721L.ttf");
        btn_auto.setTypeface(font);
        btn_media[0].setTypeface(font);
        btn_media[1].setTypeface(font);

       mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener(){

            @Override
            public void onCompletion(MediaPlayer mp) {

                if(autoplay==0) {
                    btn_media[0].setText("PLAY");
                }
                else if(autoplay==1){
                    page = viewPager.getCurrentItem();
                    switch(page){
                        case 0: viewPager.setCurrentItem(1); break;
                        case 1: viewPager.setCurrentItem(2); break;
                        case 2: viewPager.setCurrentItem(3); break;
                        case 3: viewPager.setCurrentItem(4); break;
                        case 4: viewPager.setCurrentItem(0); break;
                    }
                }
            }
        });


        mOnPageChangeListener = new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                //p = viewPager.getCurrentItem();
                Log.e("OnPageChangeListener",""+position);
                //adapter.setImages(position);
                mp.stop();
                mediaPlayer(position);

                if(mp != null)
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
        };

        viewPager.setOnPageChangeListener(mOnPageChangeListener);

        btn_auto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (autoplay==1) {
                    btn_auto.setTextColor(Color.parseColor("#BBBBBB"));
                    autoplay = 0;
                }
                else {
                    btn_auto.setTextColor(Color.parseColor("#EE4D39"));
                    autoplay = 1;
                    if(!mp.isPlaying())
                        mp.start();
                }
            }
        });

        btn_media[0].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mp.isPlaying()) {
                    mp.pause();
                    Toast.makeText(AEDActivity.this,"PAUSE", Toast.LENGTH_SHORT).show();
                    btn_media[0].setText("PLAY");
                }
                else {
                    mp.start();
                    Toast.makeText(AEDActivity.this,"PLAY", Toast.LENGTH_SHORT).show();
                    btn_media[0].setText("PAUSE");
                }
            }
        });
        btn_media[1].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mp != null) {

                    mp.stop();
                    Toast.makeText(AEDActivity.this,"STOP", Toast.LENGTH_SHORT).show();
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

    }

    @Override
    protected void onPause() {
        super.onPause();
        mp.pause();
        btn_media[0].setText("PLAY");
    }

    public void mediaPlayer(int index) {

        try {
            switch(index){
                case 0: mp.reset(); mp.setDataSource(getApplicationContext(), Uri.parse(prefix + R.raw.a_audio00)); mp.prepare(); break;
                case 1: mp.reset(); mp.setDataSource(getApplicationContext(), Uri.parse(prefix + R.raw.a_audio01)); mp.prepare(); break;
                case 2: mp.reset(); mp.setDataSource(getApplicationContext(), Uri.parse(prefix + R.raw.a_audio02)); mp.prepare(); break;
                case 3: mp.reset(); mp.setDataSource(getApplicationContext(), Uri.parse(prefix + R.raw.a_audio03)); mp.prepare(); break;
                case 4: mp.reset(); mp.setDataSource(getApplicationContext(), Uri.parse(prefix + R.raw.a_audio04)); mp.prepare(); break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}