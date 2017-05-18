# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in D:\Android-sdk/tools/proguard/proguard-android.txt
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


#-----------------混淆配置设定------------------------------------------------------------------------
#-optimizationpasses 5                                                       #指定代码压缩级别
#-dontusemixedcaseclassnames                                                 #混淆时不会产生形形色色的类名
#-dontskipnonpubliclibraryclasses                                            #指定不忽略非公共类库
#-dontpreverify                                                              #不预校验，如果需要预校验，是-dontoptimize
#-ignorewarnings                                                             #屏蔽警告
#-verbose                                                                    #混淆时记录日志
##-----------------不需要混淆系统组件等-------------------------------------------------------------------
#-keep public class * extends android.app.Activity
#-keep public class * extends android.app.Application
#-keep public class * extends android.app.Service
#-keep public class * extends android.content.BroadcastReceiver
#-keep public class * extends android.content.ContentProvider
#-keep public class * extends android.preference.Preference
#-keep public class com.android.vending.licensing.ILicensingService

#-----------------butterKnife混淆-------------------------------------------------------------------
#    -keep class butterknife.** { *; }
#    -dontwarn butterknife.internal.**
#    -keep class **$$ViewBinder { *; }
#
#    -keepclasseswithmembernames class * {
#        @butterknife.* <fields>;
#    }
#
#    -keepclassmembers class **.R$* { #不混淆资源类
#    　　public static <fields>;
#    }
#
#    -keepclasseswithmembernames class * {
#        @butterknife.* <methods>;
#    }

    # keep setters in Views so that animations can still work.
    # see http://proguard.sourceforge.net/manual/examples.html#beans
    #//所有View的子类及其子类的get、set方法都不进行混淆
#    -keepclassmembers public class * extends android.view.View {
#       void set*(***);
#       *** get*();
#    }



#    -keepclasseswithmembernames class * {  # 保持 native 方法不被混淆
#        native <methods>;
#    }
#
#     -keepclasseswithmembers class * {      # 保持自定义控件类不被混淆
#        public <init>(android.content.Context, android.util.AttributeSet, int);
#    }
#    # We want to keep methods in Activity that could be used in the XML attribute onClick
#    #//不混淆Activity中参数类型为View的所有方法
#    # 保持自定义控件类不被混淆
#    -keepclassmembers class * extends android.app.Activity {
#       public void *(android.view.View);
#    }
#
#    # For enumeration classes, see http://proguard.sourceforge.net/manual/examples.html#enumerations
#    #//不混淆Enum类型的指定方法
#    -keepclassmembers enum * {
#        public static **[] values();
#        public static ** valueOf(java.lang.String);
#    }
#
#    #//不混淆Parcelable和它的子类，还有Creator成员变量
#    -keep class * implements android.os.Parcelable {
#      public static final android.os.Parcelable$Creator *;
#    }
#
#    #//不混淆R类里及其所有内部static类中的所有static变量字段
##    -keepclassmembers class **.R$* {
#      #        public static <fields>;
#      #    }
#
#    # The support library contains references to newer platform versions.
#    # Don't warn about those in case this app is linking against an older
#    # platform version.  We know about them, and they are safe.
#    #//不提示兼容库的错误警告
#    -dontwarn android.support.**
#
#    #//不混淆Serializable接口的子类中指定的某些成员变量和方法
#    -keepclassmembers class * implements java.io.Serializable {
#        static final long serialVersionUID;
#        private static final java.io.ObjectStreamField[] serialPersistentFields;
#        private void writeObject(java.io.ObjectOutputStream);
#        private void readObject(java.io.ObjectInputStream);
#        java.lang.Object writeReplace();
#        java.lang.Object readResolve();
#    }

