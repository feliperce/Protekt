package io.github.feliperce.protekt

import android.app.Application
import di.initKoin

class DefaultApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        initKoin {

        }
    }
}