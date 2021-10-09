plugins {
    id("java-library")
}

dependencies {
    compileOnlyApi(files("libs/spigot-1.16.5.jar"))
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.5.2")
    implementation(project(":core"))
}