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
    implementation("com.linecorp.armeria:armeria-spring-boot3-starter:${property("armeriaVersion")}")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
}
