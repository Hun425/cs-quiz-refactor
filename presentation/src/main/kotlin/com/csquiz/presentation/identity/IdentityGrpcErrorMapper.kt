package com.csquiz.presentation.identity

import com.csquiz.domain.identity.MemberAlreadyExistsException
import com.csquiz.domain.identity.MemberNotFoundException
import io.grpc.Status
import io.grpc.StatusRuntimeException

object IdentityGrpcErrorMapper {
    fun map(throwable: Throwable): StatusRuntimeException {
        val status = when (throwable) {
            is IllegalArgumentException -> Status.INVALID_ARGUMENT.withDescription(throwable.message)
            is MemberAlreadyExistsException -> Status.ALREADY_EXISTS.withDescription(throwable.message)
            is MemberNotFoundException -> Status.NOT_FOUND.withDescription(throwable.message)
            else -> Status.INTERNAL.withDescription("unexpected identity error")
        }
        return status.asRuntimeException()
    }
}
