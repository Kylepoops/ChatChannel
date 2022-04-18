import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    java
    kotlin("jvm") version "1.6.20"
    id("io.izzel.taboolib") version "1.38"
}

taboolib {
    install("common")
    install("common-5")
    install("platform-bukkit")
    install("module-configuration")
    install("module-chat")
    install("module-lang")
    install("module-nms")
    install("module-nms-util")
    options("skip-kotlin")
    options("skip-kotlin-relocate")
    version = "6.0.7-54"

    description {
        bukkitApi("1.17")
        bukkitNodes = mapOf("libraries" to listOf("org.jetbrains.kotlin:kotlin-stdlib:1.6.10"))
        dependencies { name("AmazingBot") }
        contributors {
            name("小白")
            name("Kylepoops")
        }
    }
}

repositories {
    mavenCentral()
    maven("https://repo.tabooproject.org/storages/public/releases")
    maven("https://papermc.io/repo/repository/maven-public/")
    maven("https://repo.extendedclip.com/content/repositories/placeholderapi/")
}

dependencies {
    compileOnly(fileTree("libs"))
    compileOnly("io.papermc.paper:paper-api:1.17-R0.1-SNAPSHOT")
    compileOnly("me.clip:placeholderapi:2.11.1")
    compileOnly("net.mamoe:mirai-core:2.8.3")
    compileOnly(kotlin("stdlib"))
}

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_16.toString()
        freeCompilerArgs = listOf("-Xlambdas=indy", "-Xjvm-default=all")
    }
}

configure<JavaPluginExtension> {
    sourceCompatibility = JavaVersion.VERSION_16
    targetCompatibility = JavaVersion.VERSION_17
}
