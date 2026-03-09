package com.csquiz.bootstrap

import com.linecorp.armeria.common.HttpResponse
import com.linecorp.armeria.common.HttpStatus
import com.linecorp.armeria.common.MediaType
import com.linecorp.armeria.spring.ArmeriaServerConfigurator
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean

@SpringBootApplication(proxyBeanMethods = false)
class CsQuizBootstrapApplication {
    @Bean
    fun healthEndpoint(): ArmeriaServerConfigurator {
        return ArmeriaServerConfigurator { sb ->
            sb.service("/internal/health") { _, _ ->
                HttpResponse.of(HttpStatus.OK, MediaType.PLAIN_TEXT_UTF_8, "ok")
            }
        }
    }
}

fun main(args: Array<String>) {
    runApplication<CsQuizBootstrapApplication>(*args)
}
