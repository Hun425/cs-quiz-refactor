plugins {
    id("org.springframework.boot")
    id("io.spring.dependency-management")
    kotlin("plugin.spring")
}

extra["netty.version"] = "4.2.6.Final"

dependencies {
    implementation(project(":application"))
    implementation(project(":infrastructure"))
    implementation(project(":presentation"))
    implementation(project(":proto"))

    implementation("org.springframework.boot:spring-boot-starter")
    implementation("com.linecorp.armeria:armeria-spring-boot4-starter:${property("armeriaVersion")}")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.springframework.boot:spring-boot-testcontainers")
    testImplementation("org.testcontainers:junit-jupiter:${property("testcontainersVersion")}")
    testImplementation("org.testcontainers:postgresql:${property("testcontainersVersion")}")
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:${property("coroutinesVersion")}")
}

