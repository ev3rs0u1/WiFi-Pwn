package com.ev3rs0u1.wifi_pwn.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ev3rs0u1.wifi_pwn.R;
import com.ev3rs0u1.wifi_pwn.model.Json;
import com.ev3rs0u1.wifi_pwn.utils.WiFiUtils;

import java.util.ArrayList;
import java.util.Collection;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by ev3rs0u1 on 2016/7/14.
 */
public class LvAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<Json.DataBean> list;

    public LvAdapter(Context context, ArrayList<Json.DataBean> list) {
        this.context = context;
        this.list = list;
    }

    public void setData(Collection<? extends Json.DataBean> collection) {
        list.clear();
        list.addAll(collection);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.lv_item, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        Json.DataBean json = list.get(position);
        holder.tv_ssid.setText(json.getSsid());
        holder.tv_bssid.setText(json.getBssid());
        holder.tv_pwd.setText(WiFiUtils.decryptPassword(json.getPwd()));
        int level = json.getLevel();
        switch (level) {
            case 4:
                holder.img_level.setImageResource(R.drawable.highest_level);
                break;
            case 3:
                holder.img_level.setImageResource(R.drawable.high_level);
                break;
            case 2:
                holder.img_level.setImageResource(R.drawable.medium_level);
                break;
            case 1:
                holder.img_level.setImageResource(R.drawable.low_level);
                break;
            case 0:
                holder.img_level.setImageResource(R.drawable.null_level);
                break;
        }
        return convertView;
    }

    static class ViewHolder {
        @BindView(R.id.tv_ssid)
        TextView tv_ssid;
        @BindView(R.id.tv_bssid)
        TextView tv_bssid;
        @BindView(R.id.tv_pwd)
        TextView tv_pwd;
        @BindView(R.id.image_level)
        ImageView img_level;

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
