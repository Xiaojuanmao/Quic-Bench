# QUIC

QUIC为Google于2013年开发的基于UDP的多路并发传输协议，主要优势在于减少TCP三次握手及TLS握手，同时因为UDP特性在高丢包环境下依旧能正常工作。在使用情景复杂的移动网络环境下能有效降低延时以及提高请求成功率。（https://www.chromium.org/quic）

## Bench

此处选择Android作为测试平台，对同时提供QUIC和HTTP2.0支持的腾讯云CDN图片进行下载测试。

**Server（CDN）**

```Kotlin
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
```

**Client：Android**
Android平台上，我们使用从`Chromium`中抽取的[cornet](https://chromium.googlesource.com/chromium/src/+/master/components/cronet?autodive=0%2F%2F)作为QUIC Client，对比`OKHttp`作为Http2.0 Client。为了避免OKHttp本身优化的问题，我们为QUIC提供了hook OKHttp用的`Interceptor`，此数数据均为QUIC over OKHttp的测试结果。直接使用cornet engine的测试结果大家可以自行运行。

### 丢包测试

1. 模拟正常3G环境 上下行相同 Delay 0ms，Rate 750k

![bench_3g](./doc/bench_3g.png)

#### 泰国

参考[泰国网络调研](https://wiki.corp.kuaishou.com/pages/viewpage.action?pageId=27834569)数据进行测试 （调研数据中无丢包数据）

1. WIFI 

   1.1 BatWifi-Guest (Download 5.7Mbps; Upload 2.8Mbps; Ping 466ms)   

   ​

