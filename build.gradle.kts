plugins {
    kotlin("jvm") version "2.1.10"
}

group = "org.realparkourhelper"
version = "1.0"

repositories {
    mavenCentral()

    maven {
        name = "papermc"
        url = uri("https://repo.papermc.io/repository/maven-public/")
    }
}

dependencies {
    testImplementation(kotlin("test"))

    compileOnly("org.github.paperspigot:paperspigot-api:1.8.8-R0.1-20160806.221350-1")
    compileOnly("com.comphenix.protocol:ProtocolLib:4.2.1")
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(8)
}