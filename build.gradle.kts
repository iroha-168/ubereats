plugins {
    kotlin("jvm") version "2.2.20"
    id("io.ktor.plugin") version "3.3.3"
    kotlin("plugin.serialization") version "2.2.20"
}

group = "org.example"
version = "1.0-SNAPSHOT"

// TIPS: gradleタスクのTasks/application/run（./gradlew runのこと）を実行するとこのapplicationに設定したmainClassが実行される
// EngineMainはktor.server.nettyに含まれていて、ktorの設定するときにここに書く
// EngineMainが動くと、application.yamlのktor.application.modules（com.ubereats.ApplicationKt.module == fun Application.module()）を動かそうとしてくれる
application {
    mainClass = "io.ktor.server.netty.EngineMain"
}

repositories {
    mavenCentral()
}

dependencies {
    val ktorVersion = "3.3.3"
    implementation("org.jetbrains.kotlinx:kotlinx-datetime:0.7.1")
    // ktor
    implementation("io.ktor:ktor-server-core-jvm:${ktorVersion}")
    implementation("io.ktor:ktor-server-netty:${ktorVersion}")
    implementation("ch.qos.logback:logback-classic:1.4.14")
    implementation("io.ktor:ktor-server-config-yaml:${ktorVersion}")
    implementation("io.ktor:ktor-serialization-kotlinx-json:${ktorVersion}")
    implementation("io.ktor:ktor-server-content-negotiation:${ktorVersion}")
    implementation("io.ktor:ktor-server-call-logging:${ktorVersion}")
    // orm
    implementation("org.jetbrains.exposed:exposed-core:1.0.0-rc-3")
    implementation("org.jetbrains.exposed:exposed-jdbc:1.0.0-rc-3")
    implementation("org.jetbrains.exposed:exposed-kotlin-datetime:1.0.0-rc-3")
    implementation("org.postgresql:postgresql:42.7.3")
    // test
    testImplementation(kotlin("test"))
    testImplementation(kotlin("test-junit5"))
    testImplementation("org.junit.jupiter:junit-jupiter-params:6.0.1")
}

tasks.test {
    useJUnitPlatform()
}

kotlin {
    compilerOptions {
        optIn.add("kotlin.time.ExperimentalTime")
    }
}