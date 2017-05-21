package com.test.kosta.goldrone_userapplication.Http;

import android.content.Context;
import android.util.Log;

import com.test.kosta.goldrone_userapplication.DB.DataBaseHelper;
import com.test.kosta.goldrone_userapplication.DB.DroneGPS;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;

/**
 * Created by kosta on 2016-07-07.
 */
public class HttpReceive {

    Context mContext;
    public static int drone_ID;
    DataBaseHelper mHelper;
    Thread th;

    public HttpReceive(Context con, int num) {
        Log.e("HttpReceiveConstructor", "" + num);
        this.drone_ID = num;
        this.mContext = con;

        mHelper = new DataBaseHelper(mContext);

    }

    public void ReceiveInfo() {

        th = new Thread() {

            @Override
            public void run() {
                super.run();

                while (true) {
                    try {

                        HttpClient client = new DefaultHttpClient();

                        // 경로 설정 !
                        String getURL = "http://192.169.1.13:3000/posts/drone/" + drone_ID;
                        //

                        Log.e("getUrl", getURL);

                        HttpGet get = new HttpGet(getURL);
                        HttpResponse responseGet = client.execute(get);
                        HttpEntity resEntityGet = responseGet.getEntity();

                        if (resEntityGet != null) {

                            String str = EntityUtils.toString(resEntityGet);
                            JSONObject obj = new JSONObject(str);

                            double lat = Double.parseDouble(obj.get("lat").toString());
                            double lon = Double.parseDouble(obj.get("lon").toString());

                            Log.e("lat", "" + lat);
                            Log.e("lon", "" + lon);

                            List<DroneGPS> list = mHelper.getDroneGpsList();

  /*                          if(list.size() == 0){
                                mHelper.addDroneGPS(new DroneGPS(drone_ID, lat, lon));
                            }*/

                            if (list.size() == 0) {
                                mHelper.addDroneGPS(new DroneGPS(drone_ID, lat, lon));
                            } else {

                                if (list.get(list.size()-1).getLat() == lat && list.get(list.size()-1).getLon() == lon) {
                                    //
                                    Log.e("mHelper", "same");
                                } else {
                                    Log.e("mHelper", "add");
                                    mHelper.addDroneGPS(new DroneGPS(drone_ID, lat, lon));
                                }
                            }

                       }

                        Thread.sleep(10000);

                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    } catch (ClientProtocolException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }


            }
        };

        th.start();
    }

    public void stopAll(){
        try {
            th.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


}
