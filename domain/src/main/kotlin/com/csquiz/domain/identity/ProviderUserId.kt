package com.csquiz.domain.identity

@JvmInline
value class ProviderUserId private constructor(val value: String) {
    companion object {
        fun from(value: String): ProviderUserId {
            val normalized = value.trim()
            require(normalized.isNotBlank()) { "providerUserId must not be blank" }
            return ProviderUserId(normalized)
        }
    }
}
