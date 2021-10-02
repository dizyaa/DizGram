package dev.dizyaa.dizgram.feature.auth.domain

enum class AuthStatus {
    WaitPhoneNumber,
    WaitOtherDeviceConfirmation,
    WaitCode,
    WaitRegistration,
    WaitPassword,
    Ready,
}