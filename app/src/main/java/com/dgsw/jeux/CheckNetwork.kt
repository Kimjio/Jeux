package com.dgsw.jeux

import java.net.HttpURLConnection
import java.net.URL

/**
 * Created by kimji on 2017-06-23.
 */

class CheckNetwork(private val host: String) : Thread() {
    var isSuccess: Boolean = false
        private set

    override fun run() {

        var conn: HttpURLConnection? = null
        try {
            conn = URL(host).openConnection() as HttpURLConnection
            conn.setRequestProperty("User-Agent", "Android")
            conn.connectTimeout = 1000
            conn.connect()
            val responseCode = conn.responseCode
            isSuccess = responseCode == 204
        } catch (e: Exception) {
            e.printStackTrace()
            isSuccess = false
        }

        if (conn != null) {
            conn.disconnect()
        }
    }

}