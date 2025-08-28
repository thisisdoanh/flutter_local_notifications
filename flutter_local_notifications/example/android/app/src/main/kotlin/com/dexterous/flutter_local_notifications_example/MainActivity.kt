package com.dexterous.flutter_local_notifications_example

import android.app.Application
import android.content.ContentResolver
import android.content.Context
import android.media.RingtoneManager
import android.os.Bundle
import android.util.Log
import com.dexterous.flutterlocalnotifications.ScheduledNotificationHolder
import com.dexterous.flutterlocalnotifications.ScheduledNotificationITF
import com.google.firebase.Firebase
import com.google.firebase.FirebaseApp
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.analytics
import io.flutter.embedding.android.FlutterActivity
import io.flutter.embedding.engine.FlutterEngine
import io.flutter.plugin.common.MethodChannel
import java.util.*


class MainActivity: FlutterActivity() {
    override fun configureFlutterEngine(flutterEngine: FlutterEngine) {
        super.configureFlutterEngine(flutterEngine)
        MethodChannel(flutterEngine.dartExecutor.binaryMessenger, "dexterx.dev/flutter_local_notifications_example").setMethodCallHandler { call, result ->
            if ("drawableToUri" == call.method) {
                val resourceId = this@MainActivity.resources.getIdentifier(call.arguments as String, "drawable", this@MainActivity.packageName)
                result.success(resourceToUriString(this@MainActivity.applicationContext, resourceId))
            }
            if ("getAlarmUri" == call.method) {
                result.success(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM).toString())
            }
        }
    }

    private fun resourceToUriString(context: Context, resId: Int): String? {
        return (ContentResolver.SCHEME_ANDROID_RESOURCE + "://"
                + context.resources.getResourcePackageName(resId)
                + "/"
                + context.resources.getResourceTypeName(resId)
                + "/"
                + context.resources.getResourceEntryName(resId))
    }
}

class App : Application() {

    companion object {
        var instance: App? = null
        private val analytics: FirebaseAnalytics by lazy {
            Firebase.analytics
        }
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
        // Init Firebase
        FirebaseApp.initializeApp(this)


        ScheduledNotificationHolder.scheduledNotification = ScheduledNotificationITF {

            Log.d("Abc", "Logging event from ScheduledNotificationHolder")

            val bundle = Bundle().apply {
                putString("zodiac", "aries")
                putString("source", "home_screen")
                putLong("ts", System.currentTimeMillis())
            }
            App.analytics.logEvent("open_horoscope", bundle)
        }
    }
}

