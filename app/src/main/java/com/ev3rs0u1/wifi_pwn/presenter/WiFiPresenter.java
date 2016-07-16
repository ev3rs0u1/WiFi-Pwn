package com.ev3rs0u1.wifi_pwn.presenter;

/**
 * Created by ev3rs0u1 on 2016/7/14.
 */
public interface WiFiPresenter {
    void bindView();

    void checkWiFiState();

    void scanWiFiToView();
}
