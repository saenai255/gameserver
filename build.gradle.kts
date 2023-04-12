import java.nio.file.Files
import java.nio.file.Paths
import java.nio.file.StandardOpenOption
import java.util.stream.Collectors

val ktor_version: String by project
val kotlin_version: String by project
val logback_version: String by project
val protobuf_version: String by project

plugins {
    kotlin("jvm") version "1.8.20"
    id("io.ktor.plugin") version "2.2.4"
    id("com.google.protobuf") version "0.9.2"
}

group = "com.github.saenai255"
version = "0.0.1"
application {
    mainClass.set("com.github.saenai255.ApplicationKt")

    val isDevelopment: Boolean = project.ext.has("development")
    applicationDefaultJvmArgs = listOf("-Dio.ktor.development=$isDevelopment")
}

sourceSets {
    main {
        proto {
            srcDir("src/main/protobuf")
        }
    }
}

repositories {
    mavenCentral()
    gradlePluginPortal()
}

dependencies {
    implementation("io.ktor:ktor-server-core-jvm:$ktor_version")
    implementation("io.ktor:ktor-server-content-negotiation-jvm:$ktor_version")
    implementation("io.ktor:ktor-server-websockets-jvm:$ktor_version")
    implementation("io.ktor:ktor-server-netty-jvm:$ktor_version")
    implementation("ch.qos.logback:logback-classic:$logback_version")
    implementation("com.google.protobuf:protobuf-java:$protobuf_version")
    implementation("com.google.protobuf:protobuf-kotlin:$protobuf_version")
    testImplementation("io.ktor:ktor-server-tests-jvm:$ktor_version")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit:$kotlin_version")
}

tasks.findByName("generateProto")?.doLast {
    val toInterface = { it: Pair<String, Set<List<String>>> ->
        """
package com.github.saenai255.protobuf;
                
public abstract class Abstract${it.first.substring(1)} {
${
            it.second.stream()
                .map { "    public abstract ${if (it[2] == "") "void" else it[2].substring(1)} ${it[0]}(${if (it[1] == "") "" else "${it[1].substring(1)} param"});" }
                .collect(Collectors.joining("\n"))
        }
}
            """.trim()
    }

    val projectPath = Paths.get(project.projectDir.absolutePath)
    val generatedProtoFilesPath = projectPath.resolve("./build/generated/source/proto/main/java/com/github/saenai255/protobuf").normalize()

    Files.list(generatedProtoFilesPath)
        .filter { it.fileName.toString().endsWith("Service.java") && !it.fileName.toString().contains("Abstract") }
        .map {
            it.fileName.getName(it.fileName.nameCount - 1).toString().split('.').first() to Files.readAllLines(it)
                .stream()
                .filter { itt -> itt.contains("* <code>rpc ") && !itt.contains("],") }
                .map { listOf(
                    it.split("<code>rpc ")[1].split('(').first(),
                    it.split('(')[1].split(')').first(),
                    it.split("returns (")[1].split(')').first()
                ) }
                .collect(Collectors.toSet())
        }
        .map { it.first to toInterface(it) }
        .forEach { Files.write(generatedProtoFilesPath.resolve("./Abstract" + it.first.substring(1) + ".java").normalize(), it.second.toByteArray(Charsets.UTF_8), StandardOpenOption.WRITE, StandardOpenOption.CREATE) }

}
