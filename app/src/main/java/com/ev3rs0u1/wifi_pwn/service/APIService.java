package com.ev3rs0u1.wifi_pwn.service;

import com.ev3rs0u1.wifi_pwn.model.Json;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by ev3rs0u1 on 2016/7/14.
 */
public interface APIService {
    @GET("/wifi")
    Observable<Json> getJSONData(
            @Query("ssid") String ssid,
            @Query("bssid") String bssid
    );
}
