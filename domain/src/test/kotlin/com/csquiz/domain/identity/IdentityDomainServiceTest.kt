package com.csquiz.domain.identity

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Test
import java.time.Instant

class IdentityDomainServiceTest {
    private val now = Instant.parse("2026-03-10T00:00:00Z")

    @Test
    fun `registers google member with normalized email and default role`() {
        val repository = InMemoryMemberRepository()
        val service = IdentityDomainService(repository) { now }

        val member = service.register(
            RegisterMemberCommand(
                providerUserId = "google-user-1",
                email = "  User@Example.com ",
                nickname = "coder",
            ),
        )

        assertThat(member.provider).isEqualTo(AuthProvider.GOOGLE)
        assertThat(member.email.value).isEqualTo("user@example.com")
        assertThat(member.role).isEqualTo(MemberRole.USER)
        assertThat(member.createdAt).isEqualTo(now)
    }

    @Test
    fun `rejects duplicate provider user id`() {
        val repository = InMemoryMemberRepository()
        val service = IdentityDomainService(repository) { now }
        service.register(
            RegisterMemberCommand(
                providerUserId = "google-user-1",
                email = "user@example.com",
                nickname = "coder",
            ),
        )

        assertThatThrownBy {
            service.register(
                RegisterMemberCommand(
                    providerUserId = "google-user-1",
                    email = "other@example.com",
                    nickname = "other",
                ),
            )
        }.isInstanceOf(MemberAlreadyExistsException::class.java)
    }

    @Test
    fun `changes nickname for existing member`() {
        val repository = InMemoryMemberRepository()
        val service = IdentityDomainService(repository) { now }
        val member = service.register(
            RegisterMemberCommand(
                providerUserId = "google-user-1",
                email = "user@example.com",
                nickname = "coder",
            ),
        )

        val changed = service.changeNickname(
            ChangeMemberNicknameCommand(
                memberId = member.id.value,
                nickname = "architect",
            ),
        )

        assertThat(changed.nickname.value).isEqualTo("architect")
        assertThat(repository.findById(member.id)?.nickname?.value).isEqualTo("architect")
    }

    @Test
    fun `fails when changing nickname of unknown member`() {
        val repository = InMemoryMemberRepository()
        val service = IdentityDomainService(repository) { now }

        assertThatThrownBy {
            service.changeNickname(
                ChangeMemberNicknameCommand(
                    memberId = "missing-member",
                    nickname = "architect",
                ),
            )
        }.isInstanceOf(MemberNotFoundException::class.java)
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
