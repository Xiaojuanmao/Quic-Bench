package com.smilehacker.quictest

import android.annotation.SuppressLint
import android.os.AsyncTask
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.Button
import android.widget.TextView
import com.smilehacker.quicdroid.QUICDroid
import com.smilehacker.quicdroid.QUICInterceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okio.Okio
import org.chromium.net.CronetEngine
import java.io.File
import java.net.HttpURLConnection
import java.net.URL
import java.util.concurrent.TimeUnit


class MainActivity : AppCompatActivity() {

    private lateinit var mEngine: CronetEngine
    private lateinit var mOkhttpClent: OkHttpClient
    private lateinit var mOkhttpClentWithQUIC: OkHttpClient

    private val mTvResultHttp by lazy { findViewById<TextView>(R.id.tv_http_result) }
    private val mTvResultQuic by lazy { findViewById<TextView>(R.id.tv_quic_result) }
    private val mTvResultQuicOverOKHttp by lazy { findViewById<TextView>(R.id.tv_quic_ok_result) }

    companion object {
        val TAG = MainActivity::class.java.simpleName


        const val TEST_TYPE_QUIC = 1
        const val TEST_TYPE_HTTP = 2
        const val TEST_TYPE_QUIC_OVER_OKHTTP = 3
    }

    private val IMGS = arrayOf(
        "https://stgwhttp2.kof.qq.com/1.jpg",
        "https://stgwhttp2.kof.qq.com/2.jpg",
        "https://stgwhttp2.kof.qq.com/3.jpg",
        "https://stgwhttp2.kof.qq.com/4.jpg",
        "https://stgwhttp2.kof.qq.com/5.jpg",
        "https://stgwhttp2.kof.qq.com/6.jpg",
        "https://stgwhttp2.kof.qq.com/7.jpg",
        "https://stgwhttp2.kof.qq.com/8.jpg",
        "https://stgwhttp2.kof.qq.com/01.jpg",
        "https://stgwhttp2.kof.qq.com/02.jpg",
        "https://stgwhttp2.kof.qq.com/03.jpg",
        "https://stgwhttp2.kof.qq.com/04.jpg",
        "https://stgwhttp2.kof.qq.com/05.jpg",
        "https://stgwhttp2.kof.qq.com/06.jpg",
        "https://stgwhttp2.kof.qq.com/07.jpg",
        "https://stgwhttp2.kof.qq.com/08.jpg"
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        QUICDroid.init(this@MainActivity)
//
        mEngine = CronetEngine.Builder(applicationContext)
            .enableQuic(true)
            .build()

        mOkhttpClentWithQUIC = OkHttpClient.Builder()
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .connectTimeout(30, TimeUnit.SECONDS)
            .cache(null)
            .addInterceptor(QUICInterceptor())
            .build()
        mOkhttpClent = OkHttpClient.Builder()
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .connectTimeout(30, TimeUnit.SECONDS)
            .cache(null)
            .build()

        findViewById<Button>(R.id.btn_quic_test).setOnClickListener {
            doTest(TEST_TYPE_QUIC)
        }

        findViewById<Button>(R.id.btn_http_test).setOnClickListener {
            doTest(TEST_TYPE_HTTP)
        }
        findViewById<Button>(R.id.btn_quic_ok_test).setOnClickListener {
            doTest(TEST_TYPE_QUIC_OVER_OKHTTP)
        }
    }

    @SuppressLint("StaticFieldLeak")
    private fun doTest(type: Int) {
        object : AsyncTask<Void, Void, Long>() {
            override fun doInBackground(vararg params: Void?): Long {
                Log.i(TAG, "start test:")
                var cost = 0L
                for (i in 0 until IMGS.size) {
                    try {
                        cost += when(type) {

                            TEST_TYPE_HTTP -> okhttpJob(mOkhttpClent, IMGS[i])
                            TEST_TYPE_QUIC -> quicJob(IMGS[i])
                            TEST_TYPE_QUIC_OVER_OKHTTP -> okhttpJob(mOkhttpClentWithQUIC, IMGS[i])
                            else -> 0
                        }
                    } catch (e : Throwable) {
                        Log.e(TAG, "job error", e)
                    }
                }

                return cost
            }

            override fun onPostExecute(result: Long) {
                super.onPostExecute(result)
                val label = "download img ${IMGS.size} times and cost: $result ms"
                when(type) {

                    TEST_TYPE_HTTP -> mTvResultHttp.text = label
                    TEST_TYPE_QUIC -> mTvResultQuic.text = label
                    TEST_TYPE_QUIC_OVER_OKHTTP -> mTvResultQuicOverOKHttp.text = label
                }

            }

        }.execute()
    }

    private fun quicJob(img: String) : Long {

        val time = System.currentTimeMillis()


        val connection = mEngine.openConnection(URL(img)) as HttpURLConnection

        // can also hook system default
        // URL.setURLStreamHandlerFactory(mEngine.createURLStreamHandlerFactory());

        connection.requestMethod = "GET"
        connection.connect()

        val source = Okio.buffer(Okio.source(connection.inputStream))
        val downloadedFile = File(this.externalCacheDir, "a.jpg")
        val sink = Okio.buffer(Okio.sink(downloadedFile))
        sink.writeAll(source)
        sink.close()
        source.close()

        val cost = System.currentTimeMillis() - time
        Log.i(TAG, "download complete $img cost = ${System.currentTimeMillis() - time}")

        return cost
    }

    private fun okhttpJob(client: OkHttpClient, img: String) : Long {
        val time = System.currentTimeMillis()

        val req = Request.Builder()
            .url(img)
            .build()

        val resp = client.newCall(req).execute()

        val source = resp.body()!!.source()
        val downloadedFile = File(this.externalCacheDir, "b.jpg")
        val sink = Okio.buffer(Okio.sink(downloadedFile))
        sink.writeAll(source)
        sink.close()
        source.close()

        val cost = System.currentTimeMillis() - time
        Log.i(TAG, "download complete $img cost = ${System.currentTimeMillis() - time}")
        return cost
    }
}
