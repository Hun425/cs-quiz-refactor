package com.csquiz.domain.identity

data class RegisterMemberCommand(
    val providerUserId: String,
    val email: String,
    val nickname: String,
)

data class ChangeMemberNicknameCommand(
    val memberId: String,
    val nickname: String,
)
