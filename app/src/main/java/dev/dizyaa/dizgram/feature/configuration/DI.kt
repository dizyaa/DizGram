package dev.dizyaa.dizgram.feature.configuration

import org.koin.dsl.module

val configurationModule = module {
    single<Configuration> { AndroidConfiguration() }
}