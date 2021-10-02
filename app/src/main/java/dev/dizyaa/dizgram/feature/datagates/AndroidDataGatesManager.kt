package dev.dizyaa.dizgram.feature.datagates

import android.content.Context

class AndroidDataGatesManager(
    private val applicationContext: Context,
): DataGatesManager {

    override val databaseDir: String
        get() = applicationContext.filesDir.absolutePath + "/database"
    override val filesDir: String
        get() = applicationContext.filesDir.absolutePath + "/files"
}