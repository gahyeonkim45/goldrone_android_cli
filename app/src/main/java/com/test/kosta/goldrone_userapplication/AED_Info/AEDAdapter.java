package com.test.kosta.goldrone_userapplication.AED_Info;

import android.support.v4.app.*;

import com.test.kosta.goldrone_userapplication.R;

/**
 * Created by gahyeon on 2016-07-10.
 */
public class AEDAdapter extends FragmentStatePagerAdapter {

    Fragment[] fragments = new Fragment[5];
    int drawables[] = {R.drawable.a_image00, R.drawable.a_image01, R.drawable.a_image02, R.drawable.a_image03, R.drawable.a_image04};

    public AEDAdapter(android.support.v4.app.FragmentManager fm) {
        super(fm);

        for (int i = 0; i < 5; i++) {
            fragments[i] = new Fragment();
            fragments[i].setId(drawables[i]);
        }
    }

    public Fragment getItem(int arg0) {
        return fragments[arg0];
    }

    public int getCount() {
        return fragments.length;
    }

}

