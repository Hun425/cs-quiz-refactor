package com.csquiz.domain.identity

@JvmInline
value class Nickname private constructor(val value: String) {
    companion object {
        private const val MIN_LENGTH = 2
        private const val MAX_LENGTH = 20

        fun from(value: String): Nickname {
            val normalized = value.trim()
            require(normalized.length in MIN_LENGTH..MAX_LENGTH) {
                "nickname length must be between $MIN_LENGTH and $MAX_LENGTH"
            }
            return Nickname(normalized)
        }
    }
}
