package com.smilehacker.quicdroid

import okhttp3.Interceptor
import okhttp3.Protocol
import okhttp3.Response
import okhttp3.internal.http.RealResponseBody
import okio.Okio
import java.net.HttpURLConnection

/**
 * todo
 * 1. Cookie support
 *
 * Created by quan.zhou on 2018/4/13.
 */
class QUICInterceptor: Interceptor {

    private val TAG = QUICInterceptor::class.java.simpleName

    override fun intercept(chain: Interceptor.Chain): Response {
        if (!QUICDroid.enable) {
            return chain.proceed(chain.request())
        }

        val req = chain.request()

        val url = req.url().url()

        // covert okhttp request to cornet request
        val connection = QUICDroid.engine.openConnection(url) as HttpURLConnection

        // add headers
        req.headers().names().forEach {
            connection.addRequestProperty(it, req.headers()[it])
        }

        // todo pass cookie

        // method
        connection.requestMethod = req.method()

        // add body
        req.body()?.let {
            it.contentType()?.let {
                connection.setRequestProperty("Content-Type", it.toString())
            }

            connection.doOutput = true
            val os = connection.outputStream
            val sink = Okio.buffer(Okio.sink(os))
            it.writeTo(sink)
            sink.flush()
            os.close()
        }

        val statusCode = connection.responseCode

        // handling http redirect
        if (statusCode in 300..310) {
            return chain.proceed(req)
        }

        val respBuilder = Response.Builder()
        respBuilder
            .request(req)
            .protocol(Protocol.QUIC)
            .code(statusCode)
            .message(connection.responseMessage ?: "")

        val respHeaders = connection.headerFields
//        val headerBuilder = Headers.Builder()
        respHeaders.entries.forEach {
            it.value.forEach {
                    value ->
                //                headerBuilder[it.key] = value
                respBuilder.addHeader(it.key, value)

            }
        }

        val bodySource = Okio.buffer(Okio.source(
            if (statusCode in 200..399) connection.inputStream else connection.errorStream
        ))
        respBuilder.body(
            RealResponseBody(respHeaders["Content-Type"]?.last(),
                respHeaders["Content-Length"]?.last()?.toLong() ?: 0,
                bodySource)
        )
        val resp = respBuilder.build()

        return resp
    }

}