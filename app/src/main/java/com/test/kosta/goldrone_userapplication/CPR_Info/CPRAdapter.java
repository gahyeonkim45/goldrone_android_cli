package com.test.kosta.goldrone_userapplication.CPR_Info;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.test.kosta.goldrone_userapplication.R;

/**
 * Created by gahyeon on 2016-07-10.
 */
public class CPRAdapter extends FragmentStatePagerAdapter {

    com.test.kosta.goldrone_userapplication.AED_Info.Fragment[] fragments = new com.test.kosta.goldrone_userapplication.AED_Info.Fragment[5];
    int drawables[] = {R.drawable.c_image00,R.drawable.c_image01,R.drawable.c_image02,R.drawable.c_image03,R.drawable.c_image04};

    public CPRAdapter(FragmentManager fm) {
        super(fm);

        for (int i = 0; i < 5; i++) {
            fragments[i] = new com.test.kosta.goldrone_userapplication.AED_Info.Fragment();
            fragments[i].setId(drawables[i]);
        }
    }

    public com.test.kosta.goldrone_userapplication.AED_Info.Fragment getItem(int arg0) {
        return fragments[arg0];
    }

    public int getCount() {
        return fragments.length;
    }

}

