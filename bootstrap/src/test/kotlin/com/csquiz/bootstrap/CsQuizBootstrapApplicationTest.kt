package com.csquiz.bootstrap

import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest(
    properties = [
        "spring.autoconfigure.exclude=" +
            "org.springframework.boot.flyway.autoconfigure.FlywayAutoConfiguration," +
            "org.springframework.boot.jdbc.autoconfigure.DataSourceAutoConfiguration," +
            "org.springframework.boot.jooq.autoconfigure.JooqAutoConfiguration," +
            "org.springframework.boot.r2dbc.autoconfigure.R2dbcAutoConfiguration," +
            "org.springframework.boot.r2dbc.autoconfigure.R2dbcDataAutoConfiguration",
    ],
)
class CsQuizBootstrapApplicationTest {
    @Test
    fun contextLoads() {
    }
}
