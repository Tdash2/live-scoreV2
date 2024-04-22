plugins {
    id("java")
}

group = "org.aquastreams.livescore.ws"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {

}

tasks.test {
    useJUnitPlatform()
}