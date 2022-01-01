import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    java
    kotlin("jvm") version "1.6.10"
    id("io.izzel.taboolib") version "1.33"
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
    version = "6.0.7-6"

    description {
        bukkitApi("1.17")
        bukkitNodes = mapOf("libraries" to listOf("org.jetbrains.kotlin:kotlin-stdlib:1.6.10"))
        dependencies { name("AmazingBot") }
        contributors { name("小白"); name("Kylepoops") }
    }
}

repositories {
    mavenCentral()
    maven("https://repo.tabooproject.org/storages/public/releases")
    maven("https://papermc.io/repo/repository/maven-public/")
    maven("https://www.xbaimiao.com/repository/maven-releases/")
}

dependencies {
    compileOnly("io.papermc.paper:paper-api:1.17-R0.1-SNAPSHOT")
    compileOnly("com.xbaimiao:easybot:3.2.5")
    compileOnly("papi:papi:1.0.0")
    compileOnly("net.mamoe:mirai-core:2.8.3")
    compileOnly(kotlin("stdlib"))
    compileOnly(fileTree("libs"))
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
