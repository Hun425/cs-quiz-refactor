package com.csquiz.domain.identity

import java.time.Instant

data class Member(
    val id: MemberId,
    val provider: AuthProvider,
    val providerUserId: ProviderUserId,
    val email: Email,
    val nickname: Nickname,
    val role: MemberRole,
    val createdAt: Instant,
) {
    init {
        require(provider == AuthProvider.GOOGLE) { "only Google authentication is supported" }
    }

    fun changeNickname(newNickname: Nickname): Member = copy(nickname = newNickname)

    companion object {
        fun register(
            providerUserId: ProviderUserId,
            email: Email,
            nickname: Nickname,
            clock: () -> Instant = Instant::now,
        ): Member {
            return Member(
                id = MemberId.create(),
                provider = AuthProvider.GOOGLE,
                providerUserId = providerUserId,
                email = email,
                nickname = nickname,
                role = MemberRole.USER,
                createdAt = clock(),
            )
        }
    }
}
