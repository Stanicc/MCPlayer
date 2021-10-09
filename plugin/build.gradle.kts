dependencies {
    implementation(project(":common"))
}

tasks {
    processResources {
        expand("version" to project.version)
    }
}