dependencies {
    implementation("com.github.sealedtx:java-youtube-downloader:3.0.1")
    implementation("com.github.kokorin.jaffree:jaffree:2021.08.31")
    implementation("org.mapdb:mapdb:3.0.8")
    compileOnly(files("libs/spigot-1.16.5.jar"))
}

tasks {
    processResources {
        expand("version" to project.version)
    }
}