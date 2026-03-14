package com.csquiz.infrastructure.identity

import com.csquiz.domain.identity.AuthProvider
import com.csquiz.domain.identity.Email
import com.csquiz.domain.identity.Member
import com.csquiz.domain.identity.MemberId
import com.csquiz.domain.identity.MemberRepository
import com.csquiz.domain.identity.MemberRole
import com.csquiz.domain.identity.Nickname
import com.csquiz.domain.identity.ProviderUserId
import org.jooq.DSLContext
import org.jooq.Record
import org.jooq.impl.DSL.field
import org.jooq.impl.DSL.name
import org.jooq.impl.DSL.table
import org.springframework.stereotype.Repository
import java.time.OffsetDateTime
import java.time.ZoneOffset
import java.util.UUID

@Repository
class MemberJooqRepository(
    private val dsl: DSLContext,
) : MemberRepository {
    override fun findById(memberId: MemberId): Member? {
        return dsl.select(MEMBER_FIELDS)
            .from(MEMBERS)
            .where(MEMBER_ID.eq(UUID.fromString(memberId.value)))
            .fetchOne(::toDomain)
    }

    override fun findByProvider(provider: AuthProvider, providerUserId: ProviderUserId): Member? {
        return dsl.select(MEMBER_FIELDS)
            .from(MEMBERS)
            .where(PROVIDER.eq(provider.name))
            .and(PROVIDER_USER_ID.eq(providerUserId.value))
            .fetchOne(::toDomain)
    }

    override fun save(member: Member): Member {
        val updated = dsl.update(MEMBERS)
            .set(PROVIDER, member.provider.name)
            .set(PROVIDER_USER_ID, member.providerUserId.value)
            .set(EMAIL, member.email.value)
            .set(NICKNAME, member.nickname.value)
            .set(ROLE, member.role.name)
            .where(MEMBER_ID.eq(UUID.fromString(member.id.value)))
            .execute()

        if (updated == 0) {
            dsl.insertInto(MEMBERS)
                .set(MEMBER_ID, UUID.fromString(member.id.value))
                .set(PROVIDER, member.provider.name)
                .set(PROVIDER_USER_ID, member.providerUserId.value)
                .set(EMAIL, member.email.value)
                .set(NICKNAME, member.nickname.value)
                .set(ROLE, member.role.name)
                .set(CREATED_AT, OffsetDateTime.ofInstant(member.createdAt, ZoneOffset.UTC))
                .execute()
        }

        return member
    }

    private fun toDomain(record: Record): Member {
        return Member(
            id = MemberId.from(record.get(MEMBER_ID).toString()),
            provider = AuthProvider.valueOf(record.get(PROVIDER)),
            providerUserId = ProviderUserId.from(record.get(PROVIDER_USER_ID)),
            email = Email.from(record.get(EMAIL)),
            nickname = Nickname.from(record.get(NICKNAME)),
            role = MemberRole.valueOf(record.get(ROLE)),
            createdAt = record.get(CREATED_AT).toInstant(),
        )
    }

    private companion object {
        val MEMBERS = table(name("members"))

        val MEMBER_ID = field(name("member_id"), UUID::class.java)
        val PROVIDER = field(name("provider"), String::class.java)
        val PROVIDER_USER_ID = field(name("provider_user_id"), String::class.java)
        val EMAIL = field(name("email"), String::class.java)
        val NICKNAME = field(name("nickname"), String::class.java)
        val ROLE = field(name("role"), String::class.java)
        val CREATED_AT = field(name("created_at"), OffsetDateTime::class.java)

        val MEMBER_FIELDS = listOf(
            MEMBER_ID,
            PROVIDER,
            PROVIDER_USER_ID,
            EMAIL,
            NICKNAME,
            ROLE,
            CREATED_AT,
        )
    }
}
