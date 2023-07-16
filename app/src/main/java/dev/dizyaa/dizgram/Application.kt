package dev.dizyaa.dizgram

import android.app.Application
import dev.dizyaa.dizgram.core.coreModule
import dev.dizyaa.dizgram.core.telegram.CoreClient
import dev.dizyaa.dizgram.feature.auth.authModule
import dev.dizyaa.dizgram.feature.chatlist.chatListModule
import dev.dizyaa.dizgram.feature.configuration.configurationModule
import dev.dizyaa.dizgram.feature.datagates.dataGatesModule
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import org.koin.android.ext.android.get
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import timber.log.Timber

class Application: Application() {

    private var scope = MainScope()
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger()
            androidContext(this@Application)
            modules(modules)
        }
        Timber.plant(Timber.DebugTree())

        scope.launch {
            get<CoreClient>().start()
        }
    }

    override fun onLowMemory() {
        super.onLowMemory()
        scope.cancel("Low memory in Application.")
        scope = MainScope()
    }
}

val modules = listOf(
    chatListModule,
    authModule,
    configurationModule,
    dataGatesModule,
    coreModule,
)