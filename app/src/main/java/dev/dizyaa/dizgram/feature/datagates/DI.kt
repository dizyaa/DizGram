package dev.dizyaa.dizgram.feature.datagates

import org.koin.dsl.module

val dataGatesModule = module {
    single<DataGatesManager> { AndroidDataGatesManager(get()) }
}