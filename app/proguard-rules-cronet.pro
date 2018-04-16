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

# Do not obfuscate this class for testing since some of the tests check the class
# name in order to check that an instantiated engine is the Java one.
-keepnames class org.chromium.net.impl.JavaCronetEngine


# Generated for chrome apk and not included into cronet.
-dontwarn org.chromium.base.library_loader.NativeLibraries

#-keep class org.chromium.base.AnimationFrameTimeHistogram
#-keep class org.chromium.base.metrics.RecordUserAction
#-keep class org.chromium.base.JNIUtils

-keep class org.chromium.** {
    native <methods>;
}

# Understand the @Keep support annotation.
-keep class android.support.annotation.Keep

-keepclasseswithmembers class * {
    @org.chromium.base.annotations.CalledByNative <methods>;
}

