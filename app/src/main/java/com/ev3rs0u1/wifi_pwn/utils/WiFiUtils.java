package com.ev3rs0u1.wifi_pwn.utils;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * Created by ev3rs0u1 on 2016/7/10.
 */
public class WiFiUtils {
    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivity = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            NetworkInfo info = connectivity.getActiveNetworkInfo();
            if (info != null && info.isConnected()) {
                if (info.getState() == NetworkInfo.State.CONNECTED) {
                    return true;
                }
            }
        }
        return false;
    }

    public static void copyToClipboard(Context context, String str) {
        ClipboardManager cm = (ClipboardManager) context
                .getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clipData = ClipData.newPlainText("xxx", str);
        cm.setPrimaryClip(clipData);
    }

    public static String decryptPassword(String str) {
        String key = "k%7Ve#8Ie!5Fb&8E";
        String iv = "y!0Oe#2Wj#6Pw!3V";
        String result;
        byte[] b = null;
        try {
            Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding");
            SecretKeySpec keyspec = new SecretKeySpec(key.getBytes(), "AES");
            IvParameterSpec ivspec = new IvParameterSpec(iv.getBytes());
            {
                cipher.init(2, keyspec, ivspec);
                if (str != null && str.length() > 2) {
                    int i = str.length() / 2;
                    b = new byte[i];
                    for (int j = 0; j < i; j++) {
                        String tmp_string = str.substring(j * 2, j * 2 + 2);
                        b[j] = ((byte) Integer.parseInt(tmp_string, 0x10));
                    }
                }
                result = new String(cipher.doFinal(b));
            }
        } catch (Exception e) {
            return "Error";
        }
        result = result.trim();
        int pwdLength = Integer.parseInt(result.substring(0, 3));
        return result.substring(3, 3 + pwdLength);
    }

}