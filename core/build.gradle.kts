dependencies {
}

tasks {
    processResources {
        expand("version" to project.version)
    }
}