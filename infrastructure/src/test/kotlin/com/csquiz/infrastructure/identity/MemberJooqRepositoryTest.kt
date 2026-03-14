package com.csquiz.infrastructure.identity

import com.csquiz.domain.identity.AuthProvider
import com.csquiz.domain.identity.Email
import com.csquiz.domain.identity.Member
import com.csquiz.domain.identity.MemberId
import com.csquiz.domain.identity.MemberRole
import com.csquiz.domain.identity.Nickname
import com.csquiz.domain.identity.ProviderUserId
import org.assertj.core.api.Assertions.assertThat
import org.jooq.SQLDialect
import org.jooq.impl.DSL
import org.junit.jupiter.api.Test
import java.sql.DriverManager
import java.time.Instant

class MemberJooqRepositoryTest {
    @Test
    fun `saves and loads member by id and provider`() {
        DriverManager.getConnection("jdbc:h2:mem:identity;MODE=PostgreSQL;DB_CLOSE_DELAY=-1").use { connection ->
            connection.createStatement().use { statement ->
                statement.execute(
                    """
                    create table "members" (
                        "member_id" uuid primary key,
                        "provider" varchar(20) not null,
                        "provider_user_id" varchar(120) not null,
                        "email" varchar(255) not null,
                        "nickname" varchar(20) not null,
                        "role" varchar(20) not null,
                        "created_at" timestamp with time zone not null,
                        constraint uq_members_provider_user unique ("provider", "provider_user_id"),
                        constraint uq_members_email unique ("email")
                    )
                    """.trimIndent(),
                )
            }

            val repository = MemberJooqRepository(DSL.using(connection, SQLDialect.POSTGRES))
            val member = Member(
                id = MemberId.from("0f6f8df2-0ef6-4d4a-a5d2-fb11b6da4da3"),
                provider = AuthProvider.GOOGLE,
                providerUserId = ProviderUserId.from("google-user-1"),
                email = Email.from("user@example.com"),
                nickname = Nickname.from("coder"),
                role = MemberRole.USER,
                createdAt = Instant.parse("2026-03-14T00:00:00Z"),
            )

            repository.save(member)

            assertThat(repository.findById(member.id)).isEqualTo(member)
            assertThat(repository.findByProvider(member.provider, member.providerUserId)).isEqualTo(member)
        }
    }
}
