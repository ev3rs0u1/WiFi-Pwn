package com.ev3rs0u1.wifi_pwn.presenter;

import android.content.Context;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;

import com.ev3rs0u1.wifi_pwn.model.Json;
import com.ev3rs0u1.wifi_pwn.service.APIService;
import com.ev3rs0u1.wifi_pwn.utils.WiFiUtils;
import com.ev3rs0u1.wifi_pwn.view.MainView;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by ev3rs0u1 on 2016/7/14.
 */
public class WiFiPresenterImpl implements WiFiPresenter {
    private MainView mainView;
    private Context context;
    private APIService APIService;
    private WifiManager wm;
    private List<ScanResult> wifiList;
    private static final String API_URL = "http://123.207.119.59:1024";

    public WiFiPresenterImpl(MainView mainView, WifiManager wm) {
        this.mainView = mainView;
        this.context = (Context) mainView;
        this.wm = wm;
        this.wifiList = wm.getScanResults();
        APIService = new Retrofit.Builder().baseUrl(API_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build()
                .create(APIService.class);
    }

    @Override
    public void bindView() {
        try {
            checkWiFiState();
            scanWiFiToView();
        } catch (Warning w) {
            mainView.showSnackbar(w.getMessage());
        }
    }

    @Override
    public void checkWiFiState() {
        if (!wm.isWifiEnabled()) {
            wm.setWifiEnabled(true);
            throw new Warning("WiFi已关闭，正在打开中……");
        }
        if (wifiList.isEmpty())
            throw new Warning("(╯°Д°)╯ 卧槽，根本找不到WiFi信号，逗我呢？");
        if (!WiFiUtils.isNetworkAvailable(context))
            throw new Warning("请确保网络可用，以便正常使用 :)");
    }

    @Override
    public void scanWiFiToView() {
        String ssids = "";
        String bssids = "";
        final HashMap<String, Integer> wifiLevel = new HashMap<>();
        for (ScanResult wifi : wifiList) {
            ssids += "," + wifi.SSID;
            bssids += "," + wifi.BSSID;
            wifiLevel.put(wifi.BSSID, WifiManager.calculateSignalLevel(wifi.level, 5));
        }
        ssids = ssids.substring(1, ssids.length());
        bssids = bssids.substring(1, bssids.length());

        APIService.getJSONData(ssids, bssids)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Json>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        mainView.showSnackbar("WTF ??? ಥ_ಥ");
                    }

                    @Override
                    public void onNext(Json json) {
                        int level;
                        List<Json.DataBean> jsonData = json.getData();
                        for (int i = 0; i < jsonData.size(); i++) {
                            level = wifiLevel.get(jsonData.get(i).getBssid());
                            jsonData.get(i).setLevel(level);
                        }
                        Collections.reverse(jsonData);
                        mainView.updataView(jsonData);
                    }
                });
    }

    class Warning extends RuntimeException {
        public Warning(String message) {
            super(message);
        }
    }
}
