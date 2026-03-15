package com.csquiz.application.identity

import com.csquiz.domain.identity.ChangeMemberNicknameCommand
import com.csquiz.domain.identity.IdentityDomainService
import com.csquiz.domain.identity.Member
import com.csquiz.domain.identity.MemberId
import com.csquiz.domain.identity.MemberNotFoundException
import com.csquiz.domain.identity.MemberRepository
import com.csquiz.domain.identity.RegisterMemberCommand

class IdentityApplicationService(
    memberRepository: MemberRepository,
) {
    private val identityDomainService = IdentityDomainService(memberRepository)
    private val memberRepository = memberRepository

    fun register(command: RegisterIdentityCommand): Member {
        require(command.provider.equals("GOOGLE", ignoreCase = true)) {
            "only GOOGLE provider is supported"
        }

        return identityDomainService.register(
            RegisterMemberCommand(
                providerUserId = command.providerUserId,
                email = command.email,
                nickname = command.nickname,
            ),
        )
    }

    fun changeNickname(command: ChangeIdentityNicknameCommand): Member {
        return identityDomainService.changeNickname(
            ChangeMemberNicknameCommand(
                memberId = command.memberId,
                nickname = command.nickname,
            ),
        )
    }

    fun getMember(memberId: String): Member {
        return memberRepository.findById(MemberId.from(memberId))
            ?: throw MemberNotFoundException(memberId)
    }
}
