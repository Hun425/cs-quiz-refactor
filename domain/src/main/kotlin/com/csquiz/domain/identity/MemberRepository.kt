package com.csquiz.domain.identity

interface MemberRepository {
    fun findById(memberId: MemberId): Member?

    fun findByProvider(provider: AuthProvider, providerUserId: ProviderUserId): Member?

    fun save(member: Member): Member
}
