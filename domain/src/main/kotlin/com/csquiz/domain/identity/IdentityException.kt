package com.csquiz.domain.identity

sealed class IdentityException(message: String) : RuntimeException(message)

class MemberAlreadyExistsException(
    provider: AuthProvider,
    providerUserId: String,
) : IdentityException("member already exists for provider=$provider, providerUserId=$providerUserId")

class MemberNotFoundException(memberId: String) :
    IdentityException("member not found: $memberId")
