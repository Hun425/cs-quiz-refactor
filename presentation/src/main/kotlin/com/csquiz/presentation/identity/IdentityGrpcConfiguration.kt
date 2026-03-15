package com.csquiz.presentation.identity

import com.csquiz.application.identity.IdentityApplicationService
import com.csquiz.domain.identity.MemberRepository
import com.linecorp.armeria.server.grpc.GrpcService
import com.linecorp.armeria.spring.ArmeriaServerConfigurator
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration(proxyBeanMethods = false)
@ConditionalOnBean(MemberRepository::class)
class IdentityGrpcConfiguration {
    @Bean
    fun identityApplicationService(memberRepository: MemberRepository): IdentityApplicationService {
        return IdentityApplicationService(memberRepository)
    }

    @Bean
    fun identityGrpcService(identityApplicationService: IdentityApplicationService): IdentityGrpcService {
        return IdentityGrpcService(identityApplicationService)
    }

    @Bean
    fun identityGrpcServiceConfigurator(identityGrpcService: IdentityGrpcService): ArmeriaServerConfigurator {
        return ArmeriaServerConfigurator { serverBuilder ->
            serverBuilder.service(
                GrpcService.builder()
                    .addService(identityGrpcService)
                    .build(),
            )
        }
    }
}
