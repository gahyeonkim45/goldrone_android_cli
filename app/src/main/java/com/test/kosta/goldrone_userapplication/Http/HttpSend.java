package com.test.kosta.goldrone_userapplication.Http;

import android.location.Location;
import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by kosta on 2016-07-07.
 */
public class HttpSend {

    public static int SendInfo(Location param){

        try {

            HttpClient client = new DefaultHttpClient();

            // 경로 설정 ! -> gps 정보 보내는!
            String postURL = "http://192.169.1.13:3000/posts/userGPS";
            //

            HttpPost post = new HttpPost(postURL);
            List params = new ArrayList();
            params.add(new BasicNameValuePair("lat", Double.toString(param.getLatitude())));
            params.add(new BasicNameValuePair("lon",Double.toString(param.getLongitude())));
            UrlEncodedFormEntity ent = new UrlEncodedFormEntity(params, HTTP.UTF_8);
            post.setEntity(ent);

            HttpResponse responsePOST = client.execute(post);
            HttpEntity resEntity = responsePOST.getEntity();

            if (resEntity != null) {
                //Log.w("RESPONSE", EntityUtils.toString(resEntity));

                String str = EntityUtils.toString(resEntity);

                Log.e("Response",str);

                JSONObject obj = new JSONObject(str);

                // id == null일때 check!!
                if(obj.get("_id").toString().equals("null")){
                     return -1;
                }else{
                    int id = Integer.parseInt(obj.get("_id").toString());
                    Log.e("HttpSend id",""+id);
                    return id;
                }

            }else{
                Log.e("HttpSend","return null");
            }


        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return 0;

    }


}
