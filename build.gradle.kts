
plugins {
    kotlin("jvm") version "1.8.0"
    kotlin("plugin.serialization") version "1.8.0"
    id("io.ktor.plugin") version "2.2.3"
    application
}

group = "com.example"
version = "1.0-SNAPSHOT"
application {
    mainClass.set("com.example.ApplicationKt")
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("io.ktor:ktor-server-netty:2.2.3")
    implementation("io.ktor:ktor-server-host-common:2.2.3")
    implementation("io.ktor:ktor-serialization-kotlinx-json:2.2.3")
    implementation("io.ktor:ktor-server-content-negotiation:2.2.3")
    implementation("ch.qos.logback:logback-classic:1.4.12")
    testImplementation("io.ktor:ktor-server-tests:2.2.3")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit:1.8.0")
    implementation("io.ktor:ktor-client-core:2.0.0")
    implementation("io.ktor:ktor-client-cio:2.0.0")
    implementation("io.ktor:ktor-client-content-negotiation:2.0.0")
    implementation("io.ktor:ktor-serialization-kotlinx-json:2.0.0")
    implementation("org.jetbrains.kotlinx:kotlinx-cli:0.3.4")
    testImplementation("io.ktor:ktor-server-test-host:2.3.0")
    testImplementation("io.ktor:ktor-client-mock:2.3.0")
    testImplementation("org.jetbrains.kotlin:kotlin-test:1.7.0")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit:1.7.0")}
