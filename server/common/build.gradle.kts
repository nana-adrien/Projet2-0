plugins {
    id("convention.spring.service")
    alias(libs.plugins.springDependencyManagement)
}

dependencies {
    implementation(project(":client-serveur-contrat"))
    api(libs.spring.boot.starter.security)
    implementation(libs.jwt.api)
    runtimeOnly(libs.jwt.impl)
    runtimeOnly(libs.jwt.jackson)
    implementation("com.google.firebase:firebase-admin:9.3.0")

    implementation(libs.jackson.module.kotlin)
    implementation(libs.jackson.datatype.jsr310)
}
