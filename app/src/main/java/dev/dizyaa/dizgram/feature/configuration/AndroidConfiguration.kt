package dev.dizyaa.dizgram.feature.configuration

class AndroidConfiguration: Configuration {
    override val applicationVersion: String
        get() = "1.0"
    override val deviceModel: String
        get() = "some wearable device"
    override val systemLanguageCode: String
        get() = "en"
    override val systemVersion: String
        get() = "idk"
}