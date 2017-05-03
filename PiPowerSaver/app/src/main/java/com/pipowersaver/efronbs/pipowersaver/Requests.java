package com.pipowersaver.efronbs.pipowersaver;

import android.content.Context;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.ByteArrayEntity;
import cz.msebera.android.httpclient.message.BasicHeader;
import cz.msebera.android.httpclient.protocol.HTTP;

/**
 * Created by efronbs on 4/30/2017.
 */

public class Requests {

    public static void toggleDeviceState(String deviceName, ToggleState state, Context ctx, IRequestCallback callback) throws JSONException, UnsupportedEncodingException {

        AsyncHttpClient client = new AsyncHttpClient();
        JSONObject params = new JSONObject();
        params.put("device_name", deviceName);
        if (state == ToggleState.ON) {
            params.put("state", "on");
        } else {
            params.put("state", "off");
        }

        ByteArrayEntity entity = new ByteArrayEntity(params.toString().getBytes("UTF-8"));
        entity.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));

        client.post(ctx, "http://174.97.247.219:5000/toggle", entity, "application/json", new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                System.out.println("REQUEST SUCCEDED");
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                System.out.println("REQUEST FAILED: " + statusCode);
            }
        });
    }

    public static void registerNewDevice(Context ctx, final String deviceName, final IRequestCallback callback) throws JSONException, UnsupportedEncodingException {

        AsyncHttpClient client = new AsyncHttpClient();
        JSONObject params = new JSONObject();
        params.put("device_name", deviceName);

        ByteArrayEntity entity = new ByteArrayEntity(params.toString().getBytes("UTF-8"));
        entity.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));

        client.post(ctx, "http://10.0.2.2:5000/androidRegister", entity, "application/json", new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                try {
                    JSONObject response = new JSONObject(new String(responseBody));
                    String result = response.getString("data");
                    if (result.equals("exists")) {
                        callback.deviceRegisterSuccess(deviceName);
                    } else {
                        callback.deviceRegisterFailed();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                System.out.println("REGISTER NEW DEVICE FAILED: " + statusCode);
            }
        });
    }

}
