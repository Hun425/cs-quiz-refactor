package com.csquiz.presentation.identity

import com.csquiz.application.identity.ChangeIdentityNicknameCommand
import com.csquiz.application.identity.IdentityApplicationService
import com.csquiz.application.identity.RegisterIdentityCommand
import com.csquiz.proto.identity.v1.ChangeNicknameRequest
import com.csquiz.proto.identity.v1.ChangeNicknameResponse
import com.csquiz.proto.identity.v1.GetMemberRequest
import com.csquiz.proto.identity.v1.GetMemberResponse
import com.csquiz.proto.identity.v1.IdentityServiceGrpcKt
import com.csquiz.proto.identity.v1.RegisterRequest
import com.csquiz.proto.identity.v1.RegisterResponse

class IdentityGrpcService(
    private val identityApplicationService: IdentityApplicationService,
) : IdentityServiceGrpcKt.IdentityServiceCoroutineImplBase() {
    override suspend fun register(request: RegisterRequest): RegisterResponse {
        return runWithGrpcMapping {
            val member = identityApplicationService.register(
                RegisterIdentityCommand(
                    provider = request.provider,
                    providerUserId = request.providerUserId,
                    email = request.email,
                    nickname = request.nickname,
                ),
            )
            RegisterResponse.newBuilder()
                .setMemberId(member.id.value)
                .build()
        }
    }

    override suspend fun changeNickname(request: ChangeNicknameRequest): ChangeNicknameResponse {
        return runWithGrpcMapping {
            identityApplicationService.changeNickname(
                ChangeIdentityNicknameCommand(
                    memberId = request.memberId,
                    nickname = request.newNickname,
                ),
            )
            ChangeNicknameResponse.getDefaultInstance()
        }
    }

    override suspend fun getMember(request: GetMemberRequest): GetMemberResponse {
        return runWithGrpcMapping {
            val member = identityApplicationService.getMember(request.memberId)
            GetMemberResponse.newBuilder()
                .setMemberId(member.id.value)
                .setProvider(member.provider.name)
                .setEmail(member.email.value)
                .setNickname(member.nickname.value)
                .setRole(member.role.name)
                .setCreatedAt(member.createdAt.toEpochMilli())
                .build()
        }
    }

    private inline fun <T> runWithGrpcMapping(block: () -> T): T {
        return try {
            block()
        } catch (throwable: Throwable) {
            throw IdentityGrpcErrorMapper.map(throwable)
        }
    }
}
