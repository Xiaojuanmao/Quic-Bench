# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile

# Common
-ignorewarnings

# okio okhttp
-dontwarn okhttp3.**
-dontwarn okio.**
-dontwarn javax.annotation.**
-keepnames class okhttp3.internal.publicsuffix.PublicSuffixDatabase

# retrofit2
-dontnote retrofit2.Platform
-dontwarn retrofit2.Platform$Java8
-keepattributes Signature
-keepattributes Exceptions


# 删除代码中Log相关的代码
-assumenosideeffects class android.util.Log {
    public static boolean isLoggable(java.lang.String, int);
    public static int v(...);
    public static int i(...);
    public static int w(...);
    public static int d(...);
    public static int e(...);
}

# Proguard config for apps that depend on cronet_impl_native_java.jar.

# This constructor is called using the reflection from Cronet API (cronet_api.jar).
-keep class org.chromium.net.impl.NativeCronetProvider {
    public <init>(android.content.Context);
}

# Suppress unnecessary warnings.
-dontnote org.chromium.net.ProxyChangeListener$ProxyReceiver
-dontnote org.chromium.net.AndroidKeyStore
# Needs 'void setTextAppearance(int)' (API level 23).
-dontwarn org.chromium.base.ApiCompatibilityUtils
# Needs 'boolean onSearchRequested(android.view.SearchEvent)' (API level 23).
-dontwarn org.chromium.base.WindowCallbackWrapper

# Generated for chrome apk and not included into cronet.
-dontwarn org.chromium.base.library_loader.NativeLibraries
-dontwarn org.chromium.base.multidex.ChromiumMultiDexInstaller
-dontwarn org.chromium.base.metrics.CachedMetrics
-dontwarn org.chromium.base.library_loader.LibraryLoader
-dontwarn org.chromium.base.SysUtils

# Objects of this type are passed around by native code, but the class
# is never used directly by native code. Since the class is not loaded, it does
# not need to be preserved as an entry point.
-dontnote org.chromium.net.UrlRequest$ResponseHeadersMap
# https://android.googlesource.com/platform/sdk/+/marshmallow-mr1-release/files/proguard-android.txt#54
-dontwarn android.support.**

# This class should be explicitly kept to avoid failure if
# class/merging/horizontal proguard optimization is enabled.
-keep class org.chromium.base.CollectionUtil



# Proguard config for apps that depend on cronet_impl_platform_java.jar.

# This constructor is called using the reflection from Cronet API (cronet_api.jar).
-keep class org.chromium.net.impl.JavaCronetProvider {
    public <init>(android.content.Context);
}

-keepattributes Signature,InnerClasses,SourceFile,LineNumberTable
-dontwarn io.netty.**
-keep class io.netty.** { *; }
# Keep ChromiumNativeTestSupport & ChromiumPlatformOnlyTestSupport since they are
# instantiated through Reflection by the smoke tests.
-keep class org.chromium.net.smoke.ChromiumNativeTestSupport
-keep class org.chromium.net.smoke.ChromiumPlatformOnlyTestSupport

# https://android.googlesource.com/platform/sdk/+/marshmallow-mr1-release/files/proguard-android.txt#54
-dontwarn android.support.**

# Do not obfuscate this class for testing since some of the tests check the class
# name in order to check that an instantiated engine is the Java one.
-keepnames class org.chromium.net.impl.JavaCronetEngine

# These classes should be explicitly kept to avoid failure if
# class/merging/horizontal proguard optimization is enabled.
# NOTE: make sure that only test classes are added to this list.
-keep class org.chromium.base.test.** {
    *;
}

-keep class org.chromium.net.TestFilesInstaller
-keep class org.chromium.net.MetricsTestUtil

# Generated for chrome apk and not included into cronet.
-dontwarn org.chromium.base.library_loader.NativeLibraries


-keep class org.chromium.** {*;}
-dontwarn org.chromium.**
