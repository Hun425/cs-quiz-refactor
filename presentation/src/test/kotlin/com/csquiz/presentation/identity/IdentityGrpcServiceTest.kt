package com.csquiz.presentation.identity

import com.csquiz.application.identity.IdentityApplicationService
import com.csquiz.domain.identity.AuthProvider
import com.csquiz.domain.identity.Email
import com.csquiz.domain.identity.Member
import com.csquiz.domain.identity.MemberId
import com.csquiz.domain.identity.MemberRepository
import com.csquiz.domain.identity.MemberRole
import com.csquiz.domain.identity.Nickname
import com.csquiz.domain.identity.ProviderUserId
import com.csquiz.proto.identity.v1.GetMemberRequest
import com.csquiz.proto.identity.v1.RegisterRequest
import io.grpc.Status
import java.time.Instant
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlinx.coroutines.runBlocking

class IdentityGrpcServiceTest {
    @Test
    fun `register maps unsupported provider to invalid argument`() {
        val service = IdentityGrpcService(IdentityApplicationService(InMemoryMemberRepository()))

        val exception = assertFailsWith<io.grpc.StatusRuntimeException> {
            runBlocking {
                service.register(
                    RegisterRequest.newBuilder()
                        .setProvider("KAKAO")
                        .setProviderUserId("provider-user")
                        .setEmail("user@example.com")
                        .setNickname("coder")
                        .build(),
                )
            }
        }

        assertEquals(Status.INVALID_ARGUMENT.code, exception.status.code)
    }

    @Test
    fun `get member maps domain member to proto response`() {
        val repository = InMemoryMemberRepository().apply {
            save(
                Member(
                    id = MemberId.from("0f6f8df2-0ef6-4d4a-a5d2-fb11b6da4da3"),
                    provider = AuthProvider.GOOGLE,
                    providerUserId = ProviderUserId.from("provider-user"),
                    email = Email.from("user@example.com"),
                    nickname = Nickname.from("coder"),
                    role = MemberRole.USER,
                    createdAt = Instant.parse("2026-03-15T00:00:00Z"),
                ),
            )
        }
        val service = IdentityGrpcService(IdentityApplicationService(repository))

        val response = runBlocking {
            service.getMember(
                GetMemberRequest.newBuilder()
                    .setMemberId("0f6f8df2-0ef6-4d4a-a5d2-fb11b6da4da3")
                    .build(),
            )
        }

        assertEquals("GOOGLE", response.provider)
        assertEquals("user@example.com", response.email)
        assertEquals("coder", response.nickname)
        assertEquals("USER", response.role)
        assertEquals(1773532800000, response.createdAt)
    }

    private class InMemoryMemberRepository : MemberRepository {
        private val storage = linkedMapOf<String, Member>()

        override fun findById(memberId: MemberId): Member? = storage[memberId.value]

        override fun findByProvider(provider: AuthProvider, providerUserId: ProviderUserId): Member? {
            return storage.values.firstOrNull {
                it.provider == provider && it.providerUserId == providerUserId
            }
        }

        override fun save(member: Member): Member {
            storage[member.id.value] = member
            return member
        }
    }
}
