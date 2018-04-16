package com.smilehacker.quicdroid

import android.annotation.SuppressLint
import android.content.Context
import org.chromium.net.CronetEngine

@SuppressLint("StaticFieldLeak")
/**
 * Created by quan.zhou on 2018/4/16.
 */
object QUICDroid {

    internal lateinit var context: Context
    internal lateinit var engine: CronetEngine

    @Volatile
    var enable: Boolean = true

    fun init(context: Context) {
        init(context, defaultEngine(context))
    }

    fun init(context: Context, engine: CronetEngine) {
        this.context = context.applicationContext
        this.engine = engine
    }

    private fun defaultEngine(context: Context) =
        CronetEngine.Builder(context.applicationContext)
            .enableQuic(true)
            .enableHttp2(true)
            .build()
}