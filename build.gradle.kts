plugins {
    kotlin("jvm") version "1.7.21"
    application
    id("com.github.johnrengelman.shadow") version "7.1.2"
}

group = "org.some.project.kotlin"
version = "1.0"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))
}

application {
    mainClass.set("org.some.project.kotlin.MainKt")
}
