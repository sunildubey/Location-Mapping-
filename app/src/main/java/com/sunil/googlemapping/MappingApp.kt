package com.sunil.googlemapping

import android.app.Application
import com.sunil.googlemapping.di.viewModelModule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.InternalCoroutinesApi
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin


@ExperimentalCoroutinesApi
@InternalCoroutinesApi
class MappingApp : Application() {
    override fun onCreate() {
        super.onCreate()
        // start Koin dependency framework
        startKoin {
            // declare used Android context
            androidContext(applicationContext)
            // declare modules
            modules(viewModelModule)
        }
    }
}