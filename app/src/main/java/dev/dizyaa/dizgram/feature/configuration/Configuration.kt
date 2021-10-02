package dev.dizyaa.dizgram.feature.configuration

interface Configuration {
    val systemLanguageCode: String
    val deviceModel: String
    val systemVersion: String
    val applicationVersion: String
}