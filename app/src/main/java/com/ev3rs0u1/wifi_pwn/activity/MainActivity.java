package com.ev3rs0u1.wifi_pwn.activity;

import android.content.Context;
import android.graphics.Color;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.androidadvance.topsnackbar.TSnackbar;
import com.baoyz.widget.PullRefreshLayout;
import com.ev3rs0u1.wifi_pwn.R;
import com.ev3rs0u1.wifi_pwn.adapter.LvAdapter;
import com.ev3rs0u1.wifi_pwn.model.Json;
import com.ev3rs0u1.wifi_pwn.presenter.WiFiPresenter;
import com.ev3rs0u1.wifi_pwn.presenter.WiFiPresenterImpl;
import com.ev3rs0u1.wifi_pwn.utils.WiFiUtils;
import com.ev3rs0u1.wifi_pwn.view.MainView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnItemClick;

public class MainActivity extends AppCompatActivity implements MainView, PullRefreshLayout.OnRefreshListener {
    private LvAdapter adapter;
    private ArrayList<Json.DataBean> jsonData;

    @BindView(R.id.swipeRefreshLayout)
    PullRefreshLayout layout;
    @BindView(R.id.lv_wifi)
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initView();
        initWiFiView();
    }

    private void initView() {
        jsonData = new ArrayList<>();
        adapter = new LvAdapter(this, jsonData);
        listView.setAdapter(adapter);
        layout.setOnRefreshListener(this);
        layout.setRefreshStyle(PullRefreshLayout.STYLE_RING);
    }

    private void initWiFiView() {
        WifiManager wifiManager = (WifiManager) this.getSystemService(Context.WIFI_SERVICE);
        WiFiPresenter wifiPresenter = new WiFiPresenterImpl(this, wifiManager);
        wifiPresenter.bindView();
    }

    @Override
    public void onRefresh() {
        layout.postDelayed(new Runnable() {
            @Override
            public void run() {
                initWiFiView();
                layout.setRefreshing(false);
            }
        }, 1000);
    }

    @OnItemClick(R.id.lv_wifi)
    public void onClick(AdapterView<?> parent, View view, int position, long id) {
        String pwd = WiFiUtils.decryptPassword(jsonData.get(position).getPwd());
        WiFiUtils.copyToClipboard(this, pwd);
        showSnackbar("密码：" + pwd + " 以复制到剪切板");
    }

    @Override
    public void updataView(List<Json.DataBean> jsonData) {
        if (jsonData.size() > 0)
            adapter.setData(jsonData);
        else
            showSnackbar("╮(╯▽╰)╭ 未找到可破解的WiFi...");
    }

    @Override
    public void showSnackbar(String msg) {
        TSnackbar snackbar = TSnackbar
                .make(findViewById(android.R.id.content), msg, TSnackbar.LENGTH_SHORT);
        View snackbarView = snackbar.getView();
        snackbarView.setBackgroundColor(Color.parseColor("#546E7A"));
        snackbar.show();
    }
}
