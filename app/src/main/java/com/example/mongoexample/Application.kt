package com.example.mongoexample

import android.app.Application
import io.realm.Realm
import io.realm.mongodb.App
import io.realm.mongodb.AppConfiguration

const val appID:String = BuildConfig.MONGODB_APP_ID
lateinit var app : App
open class Application : Application() {
    private lateinit var uiThreadRealm: Realm

    @Override
    override fun onCreate() {
        super.onCreate()
        Realm.init(this)
        app = App(AppConfiguration.Builder(appID)
            .build())

    }
}