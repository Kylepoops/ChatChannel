plugins {
    java
    id("io.izzel.taboolib") version "1.34"
    id("org.jetbrains.kotlin.jvm") version "1.6.10"
}

taboolib {
    description {
        contributors {
            name("小白").description("TabooLib Developer")
        }
        dependencies {
            name("AmazingBot")
        }
    }
    install("common")
    install("common-5")
    install("platform-bukkit")
    install("module-configuration")
    install("module-chat")
    install("module-lang")
    install("module-nms")
    install("module-nms-util")
    version = "6.0.7-36"
}

repositories {
    mavenCentral()
    maven(url = uri("https://run.xbaimiao.com/nexus/repository/maven-releases/"))
}

dependencies {
    compileOnly("ink.ptms.core:v11701:11701:mapped")
    compileOnly("ink.ptms.core:v11701:11701:universal")
    compileOnly("public:papi:1.0.0")
    implementation("net.mamoe:mirai-core:2.7.1")
    compileOnly(kotlin("stdlib"))
    compileOnly(fileTree("libs"))
}

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
}

configure<JavaPluginConvention> {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}