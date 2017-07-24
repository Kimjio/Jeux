package com.dgsw.jeux;

import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by kimji on 2017-06-23.
 */

public class CheckNetwork extends Thread {
    private boolean success;
    private String host;

    public CheckNetwork(String host) {
        this.host = host;
    }

    @Override
    public void run() {

        HttpURLConnection conn = null;
        try {
            conn = (HttpURLConnection) new URL(host).openConnection();
            conn.setRequestProperty("User-Agent", "Android");
            conn.setConnectTimeout(1000);
            conn.connect();
            int responseCode = conn.getResponseCode();
            success = (responseCode == 204);
        } catch (Exception e) {
            e.printStackTrace();
            success = false;
        }
        if (conn != null) {
            conn.disconnect();
        }
    }

    public boolean isSuccess() {
        return success;
    }

}