package com.pipowersaver.efronbs.pipowersaver;

/**
 * Created by efronbs on 5/2/2017.
 */

public interface IRequestCallback {

    void deviceRegisterSuccess(String deviceName);
    void deviceRegisterFailed();
}
