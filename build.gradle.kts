plugins {
    kotlin("jvm") version "1.9.25" apply false
}

allprojects {
    group = "com.csquiz"
    version = "0.1.0-SNAPSHOT"

    repositories {
        mavenCentral()
    }
}

subprojects {
    apply(plugin = "org.jetbrains.kotlin.jvm")

    dependencies {
        "testImplementation"(kotlin("test"))
        "testImplementation"("org.junit.jupiter:junit-jupiter:5.12.2")
    }

    tasks.withType<Test>().configureEach {
        useJUnitPlatform()
    }

    kotlin {
        jvmToolchain(21)
    }
}
