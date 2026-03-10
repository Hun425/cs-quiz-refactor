package com.csquiz.domain.identity

import java.time.Instant

class IdentityDomainService(
    private val memberRepository: MemberRepository,
    private val clock: () -> Instant = Instant::now,
) {
    fun register(command: RegisterMemberCommand): Member {
        val providerUserId = ProviderUserId.from(command.providerUserId)
        val existing = memberRepository.findByProvider(AuthProvider.GOOGLE, providerUserId)
        if (existing != null) {
            throw MemberAlreadyExistsException(AuthProvider.GOOGLE, providerUserId.value)
        }

        val member = Member.register(
            providerUserId = providerUserId,
            email = Email.from(command.email),
            nickname = Nickname.from(command.nickname),
            clock = clock,
        )
        return memberRepository.save(member)
    }

    fun changeNickname(command: ChangeMemberNicknameCommand): Member {
        val memberId = MemberId.from(command.memberId)
        val member = memberRepository.findById(memberId)
            ?: throw MemberNotFoundException(memberId.value)

        val changed = member.changeNickname(Nickname.from(command.nickname))
        return memberRepository.save(changed)
    }
}
