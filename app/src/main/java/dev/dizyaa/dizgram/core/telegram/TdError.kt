package dev.dizyaa.dizgram.core.telegram

import org.drinkless.td.libcore.telegram.TdApi

class TdError(error: TdApi.Error): RuntimeException("${error.code}: ${error.message}")