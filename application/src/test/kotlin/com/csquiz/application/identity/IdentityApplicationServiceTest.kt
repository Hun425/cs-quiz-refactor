package com.csquiz.application.identity

import com.csquiz.domain.identity.AuthProvider
import com.csquiz.domain.identity.Email
import com.csquiz.domain.identity.Member
import com.csquiz.domain.identity.MemberAlreadyExistsException
import com.csquiz.domain.identity.MemberId
import com.csquiz.domain.identity.MemberNotFoundException
import com.csquiz.domain.identity.MemberRepository
import com.csquiz.domain.identity.MemberRole
import com.csquiz.domain.identity.Nickname
import com.csquiz.domain.identity.ProviderUserId
import java.time.Instant
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class IdentityApplicationServiceTest {
    @Test
    fun `register accepts only google provider`() {
        val service = IdentityApplicationService(InMemoryMemberRepository())

        val exception = assertFailsWith<IllegalArgumentException> {
            service.register(
                RegisterIdentityCommand(
                    provider = "KAKAO",
                    providerUserId = "provider-user",
                    email = "user@example.com",
                    nickname = "coder",
                ),
            )
        }

        assertEquals("only GOOGLE provider is supported", exception.message)
    }

    @Test
    fun `get member throws when member is missing`() {
        val service = IdentityApplicationService(InMemoryMemberRepository())

        assertFailsWith<MemberNotFoundException> {
            service.getMember("missing-member")
        }
    }

    @Test
    fun `register throws when duplicate provider user exists`() {
        val repository = InMemoryMemberRepository()
        val service = IdentityApplicationService(repository)
        service.register(
            RegisterIdentityCommand(
                provider = "GOOGLE",
                providerUserId = "provider-user",
                email = "user@example.com",
                nickname = "coder",
            ),
        )

        assertFailsWith<MemberAlreadyExistsException> {
            service.register(
                RegisterIdentityCommand(
                    provider = "GOOGLE",
                    providerUserId = "provider-user",
                    email = "other@example.com",
                    nickname = "other",
                ),
            )
        }
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
