package com.csquiz.application.identity

data class RegisterIdentityCommand(
    val provider: String,
    val providerUserId: String,
    val email: String,
    val nickname: String,
)

data class ChangeIdentityNicknameCommand(
    val memberId: String,
    val nickname: String,
)
