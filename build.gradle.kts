plugins {
    java
    kotlin("jvm") version "1.5.31" apply false
    id("com.github.johnrengelman.shadow") version "7.0.0"
}

subprojects {
    apply {
        plugin("java")
        plugin("org.jetbrains.kotlin.jvm")
        plugin("com.github.johnrengelman.shadow")
    }

    group = "stanic.mcplayer"
    version = "1.0.0"

    repositories {
        mavenLocal()
        mavenCentral()
        maven { url = uri("https://jitpack.io/") }
        maven { url = uri("https://oss.sonatype.org/content/repositories/central/") }
        maven { url = uri("https://oss.sonatype.org/content/repositories/snapshots/") }
        maven { url = uri("https://hub.spigotmc.org/nexus/content/repositories/snapshots/") }
    }

    dependencies {
        implementation(kotlin("stdlib"))
    }

    tasks {
        build { dependsOn(shadowJar) }
        shadowJar {
            archiveFileName.set("${rootProject.name}-${this@subprojects.name}-${this@subprojects.version}.jar")
        }
        withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
            kotlinOptions.jvmTarget = "1.8"
        }
    }
}