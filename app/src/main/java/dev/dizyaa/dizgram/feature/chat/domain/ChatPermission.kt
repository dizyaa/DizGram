package dev.dizyaa.dizgram.feature.chat.domain

enum class ChatPermission {
    SendTextMessage,
    // media
    SendPhotos,
    SendVideo,
    SendMusic,
    SendFiles,
    SendStickerAndGifs,
    SendVoiceMessage,
    SendVideoMessage,
    SendPolls,
    SendEmbedLinks,
    //other
    AddMember,
    ChangeGroupInfo,
    PinMessages,
}