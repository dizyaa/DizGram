package dev.dizyaa.dizgram

import android.app.Application
import dev.dizyaa.dizgram.core.coreModule
import dev.dizyaa.dizgram.core.telegram.TelegramContext
import dev.dizyaa.dizgram.feature.auth.authModule
import dev.dizyaa.dizgram.feature.chat.chatModule
import dev.dizyaa.dizgram.feature.chatlist.chatListModule
import dev.dizyaa.dizgram.feature.configuration.configurationModule
import dev.dizyaa.dizgram.feature.datagates.dataGatesModule
import dev.dizyaa.dizgram.feature.user.userModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.dsl.module
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

val appModule = module {
    single<TelegramContext>(createdAtStart = true) { TelegramContext() }
}

val modules = listOf(
    appModule,
    chatListModule,
    authModule,
    configurationModule,
    dataGatesModule,
    coreModule,
    userModule,
    chatModule,
)