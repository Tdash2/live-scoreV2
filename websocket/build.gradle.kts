import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
    id("java")
    id("com.github.johnrengelman.shadow") version "8.1.1"
}

group = "org.aquastreams.livescore.ws"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.glassfish.tyrus:tyrus-server:1.1") // yes, this is old.
    implementation("org.glassfish.tyrus:tyrus-container-grizzly:1.1") // this too

    implementation("redis.clients:jedis:2.8.0")

    compileOnly("org.projectlombok:lombok:1.18.32")
    annotationProcessor("org.projectlombok:lombok:1.18.32")

    implementation("org.slf4j:slf4j-api:2.0.13")
    implementation("ch.qos.logback:logback-core:1.5.5")
    implementation("ch.qos.logback:logback-classic:1.5.5")
}

tasks.withType<ShadowJar> {
    archiveBaseName.set("livescore-websocket")
    archiveClassifier.set("")
    archiveVersion.set("")
}

tasks.withType<Jar> {
    manifest {
        attributes["Main-Class"] = "org.aquastreams.livescore.ws.Main"
    }
}