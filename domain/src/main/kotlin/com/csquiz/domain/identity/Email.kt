package com.csquiz.domain.identity

@JvmInline
value class Email private constructor(val value: String) {
    companion object {
        private val EMAIL_REGEX = Regex("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")

        fun from(value: String): Email {
            val normalized = value.trim().lowercase()
            require(normalized.isNotBlank()) { "email must not be blank" }
            require(EMAIL_REGEX.matches(normalized)) { "email must be valid" }
            return Email(normalized)
        }
    }
}
