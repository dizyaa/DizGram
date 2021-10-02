package dev.dizyaa.dizgram

import android.app.Application
import dev.dizyaa.dizgram.feature.auth.authModule
import dev.dizyaa.dizgram.feature.chatlist.chatListModule
import dev.dizyaa.dizgram.feature.configuration.configurationModule
import dev.dizyaa.dizgram.feature.datagates.dataGatesModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import timber.log.Timber

class Application: Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger()
            androidContext(this@Application)
            modules(modules)
        }
        Timber.plant(Timber.DebugTree())
    }
}

val modules = listOf(
    chatListModule,
    authModule,
    configurationModule,
    dataGatesModule,
)