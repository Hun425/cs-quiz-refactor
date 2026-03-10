package com.csquiz.domain.identity

import java.util.UUID

@JvmInline
value class MemberId private constructor(val value: String) {
    companion object {
        fun create(): MemberId = MemberId(UUID.randomUUID().toString())

        fun from(value: String): MemberId {
            require(value.isNotBlank()) { "memberId must not be blank" }
            return MemberId(value)
        }
    }
}
