package com.test.kosta.goldrone_userapplication.AED_Info;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;


import com.test.kosta.goldrone_userapplication.R;

/**
 * Created by gahyeon on 2016-07-10.
 */
public class Fragment extends android.support.v4.app.Fragment {

    ImageView imgView;
    View view;
    int id;

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        imgView = (ImageView) view.findViewById(R.id.imageView);

        if (id == 0) {
            imgView.setBackground(null);
        } else {
            imgView.setBackgroundResource(id);
        }

    }


    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment, container, false);
        return view;
    }

}
