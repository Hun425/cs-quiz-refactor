package com.csquiz.bootstrap

import com.csquiz.presentation.identity.IdentityGrpcService
import com.csquiz.proto.identity.v1.ChangeNicknameRequest
import com.csquiz.proto.identity.v1.GetMemberRequest
import com.csquiz.proto.identity.v1.RegisterRequest
import io.grpc.Status
import io.grpc.StatusRuntimeException
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

@SpringBootTest
@Testcontainers(disabledWithoutDocker = true)
class IdentityIntegrationTest {
    @Autowired
    lateinit var identityGrpcService: IdentityGrpcService

    @Test
    fun `register get and change nickname through wired grpc service`() = runBlocking {
        val registerResponse = identityGrpcService.register(
            RegisterRequest.newBuilder()
                .setProvider("GOOGLE")
                .setProviderUserId("google-user-1")
                .setEmail("user@example.com")
                .setNickname("coder")
                .build(),
        )

        val memberResponse = identityGrpcService.getMember(
            GetMemberRequest.newBuilder()
                .setMemberId(registerResponse.memberId)
                .build(),
        )

        assertEquals("GOOGLE", memberResponse.provider)
        assertEquals("user@example.com", memberResponse.email)
        assertEquals("coder", memberResponse.nickname)

        identityGrpcService.changeNickname(
            ChangeNicknameRequest.newBuilder()
                .setMemberId(registerResponse.memberId)
                .setNewNickname("architect")
                .build(),
        )

        val changedResponse = identityGrpcService.getMember(
            GetMemberRequest.newBuilder()
                .setMemberId(registerResponse.memberId)
                .build(),
        )

        assertEquals("architect", changedResponse.nickname)
    }

    @Test
    fun `duplicate register maps to already exists`() = runBlocking {
        identityGrpcService.register(
            RegisterRequest.newBuilder()
                .setProvider("GOOGLE")
                .setProviderUserId("google-duplicate")
                .setEmail("duplicate@example.com")
                .setNickname("coder")
                .build(),
        )

        val exception = assertFailsWith<StatusRuntimeException> {
            identityGrpcService.register(
                RegisterRequest.newBuilder()
                    .setProvider("GOOGLE")
                    .setProviderUserId("google-duplicate")
                    .setEmail("other@example.com")
                    .setNickname("other")
                    .build(),
            )
        }

        assertEquals(Status.ALREADY_EXISTS.code, exception.status.code)
    }

    companion object {
        @Container
        @JvmStatic
        val postgres: PostgreSQLContainer<*> = PostgreSQLContainer("postgres:17-alpine")
            .withDatabaseName("cs_quiz")
            .withUsername("csquiz")
            .withPassword("csquiz")

        @JvmStatic
        @DynamicPropertySource
        fun databaseProperties(registry: DynamicPropertyRegistry) {
            registry.add("spring.datasource.url") { postgres.jdbcUrl }
            registry.add("spring.datasource.username") { postgres.username }
            registry.add("spring.datasource.password") { postgres.password }
            registry.add("spring.r2dbc.url") {
                "r2dbc:postgresql://${postgres.host}:${postgres.firstMappedPort}/${postgres.databaseName}"
            }
            registry.add("spring.r2dbc.username") { postgres.username }
            registry.add("spring.r2dbc.password") { postgres.password }
            registry.add("spring.flyway.enabled") { true }
        }
    }
}
