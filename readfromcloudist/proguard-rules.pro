# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in /home/mree/Android/Sdk/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}
-keep class com.daimajia.swipe.** { *; }
#-keep class com.google.** { *; }
-keep class com.squareup.** { *; }
-keep class org.joda.** { *; }
#-keep class org.springframework.http.** { *; }
-keep class retrofit.appengine.** { *; }
-keep class org.codehaus.** { *; }
-keep class org.apache.commons.** { *; }
-keep class java.nio.file.** { *; }
-keep class mree.cloud.music.player.common.** { *; }

-keep enum com.daimajia.swipe.** { *; }
#-keep enum com.google.** { *; }
-keep enum com.squareup.** { *; }
-keep enum org.joda.** { *; }
#-keep enum org.springframework.http.** { *; }
-keep enum retrofit.appengine.** { *; }
-keep enum org.codehaus.** { *; }
-keep enum org.apache.commons.** { *; }
-keep enum java.nio.file.** { *; }
-keep enum mree.cloud.music.player.common.** { *; }

-dontwarn rx.**
-dontwarn com.squareup.okhttp.*
-keepattributes *Annotation*,Signature


-keepattributes Signature
#-keepattributes InnerClasses


#-injars libs/rc.jar
#-libraryjars libs/jackson-core-2.0.0.jar

#-dontskipnonpubliclibraryclassmembers

-keepattributes *Annotation*,EnclosingMethod

-keepnames class org.codehaus.jackson.** { *; }

-dontwarn javax.xml.**
-dontwarn javax.naming.**
-dontwarn retrofit.**
-dontwarn joda.**
-dontwarn javax.xml.stream.events.**
-dontwarn com.fasterxml.jackson.databind.**
-dontwarn org.apache.lang.**
-dontwarn android.net.http.AndroidHttpClient
-dontwarn com.google.android.gms.**
-dontwarn com.android.volley.toolbox.**
-dontwarn com.squareup.okhttp.**
-dontwarn org.springframework.http.client.**
-dontwarn org.springframework.http.converter.feed.**
-dontwarn org.springframework.http.converter.json.**
-dontwarn org.springframework.http.converter.xml.**
-dontwarn okio.**
#-keepnames class org.spongycastle.jce.provider.*
#-keepnames public class org.spongycastle.x509.util.LDAPStoreHelper

-keep class com.android.vending.billing.**
-keep class org.codehaus.jackson.** { *; }

-keepclasseswithmembernames class org.springframework.http.converter.json.**{*;}
-keepclasseswithmembernames enum org.springframework.http.converter.json.** {*;}
-keepclasseswithmembernames interface org.springframework.http.converter.json.** {*;}

-keep class com.google.**{ *; }
-keep enum com.google.**{*; }
-keep interface com.google.**{*;}

-dontwarn org.joda.convert.**
-dontwarn org.joda.time.**
-keep class org.joda.time.**{ *; }
-keep enum org.joda.time.**{*; }
-keep interface org.joda.time.**{*;}

-keep class android.**{ *; }
-keep enum android.**{*; }
-keep interface android.**{*;}

-keep class com.android.**{ *; }
-keep enum com.android.**{*; }
-keep interface com.android.**{*;}

-keep class android.app.AppOpsManager
-keep class com.google.android.chimera.Activity

-keepclasseswithmembernames class com.mitril.mcys.server.common.** {*;}
-keepclasseswithmembernames enum com.mitril.mcys.server.common.** {*;}

-keepparameternames

-keep class com.fasterxml.jackson.annotation.** {*;}
-keep enum com.fasterxml.jackson.annotation.** {*;}
-keep interface com.fasterxml.jackson.annotation.** {*;}

-keep class com.google.android.gms.** {*;}
-keep enum com.google.android.gms.** {*;}
-keep interface com.google.android.gms.** {*;}

-keep class mree.cloud.music.player.common.** {*;}
-keep enum mree.cloud.music.player.common.** {*;}
-keep interface mree.cloud.music.player.common.** {*;}

-keep class org.springframework.**
-keep class org.springframework.** { *; }
-keepnames class org.springframework.** { *; }
-keepclasseswithmembernames class org.springframework.**{*;}
-keepclasseswithmembernames enum org.springframework.** {*;}
-keepclasseswithmembernames interface org.springframework.** {*;}

-keep class org.springframework.http.**{ *; }
-keep enum org.springframework.http.**{*; }
-keep interface org.springframework.http.**{*;}


-keepclasseswithmembernames class com.fasterxml.jackson.** {*;}
-keepclasseswithmembernames interface com.fasterxml.jackson.** {*;}
-keep class com.spotify.sdk.android.** { *; }
