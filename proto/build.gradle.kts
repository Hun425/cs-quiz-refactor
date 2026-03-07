plugins {
    id("com.google.protobuf")
}

dependencies {
    api("io.grpc:grpc-stub:${property("grpcVersion")}")
    api("io.grpc:grpc-protobuf:${property("grpcVersion")}")
    api("io.grpc:grpc-kotlin-stub:${property("grpcKotlinVersion")}")
    api("com.google.protobuf:protobuf-kotlin:${property("protobufVersion")}")

    compileOnly("javax.annotation:javax.annotation-api:1.3.2")
}

protobuf {
    protoc {
        artifact = "com.google.protobuf:protoc:${property("protobufVersion")}"
    }
    plugins {
        create("grpc") {
            artifact = "io.grpc:protoc-gen-grpc-java:${property("grpcVersion")}"
        }
        create("grpckt") {
            artifact = "io.grpc:protoc-gen-grpc-kotlin:${property("grpcKotlinVersion")}:jdk8@jar"
        }
    }
    generateProtoTasks {
        all().configureEach {
            plugins {
                create("grpc")
                create("grpckt")
            }
            builtins {
                create("kotlin")
            }
        }
    }
}
