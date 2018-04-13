package com.smilehacker.quictest

import org.chromium.net.CronetEngine

/**
 * Created by quan.zhou on 2018/4/13.
 */
object QUICClient {

    val engine by lazy {
        CronetEngine.Builder(App.INSTANCE)
            .enableQuic(true)
            .build()
    }
}