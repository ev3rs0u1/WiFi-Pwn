package com.ev3rs0u1.wifi_pwn.view;

import com.ev3rs0u1.wifi_pwn.model.Json;

import java.util.List;

/**
 * Created by ev3rs0u1 on 2016/7/14.
 */
public interface MainView {
    void showSnackbar(String msg);

    void updataView(List<Json.DataBean> json);
}
