plugins {
    id("convention.spring.service")
    alias(libs.plugins.springDependencyManagement)
    alias(libs.plugins.kotlinJpa)
}

dependencies {
    implementation(project(":server:common"))
    implementation(project(":server:user"))
    implementation(project(":server:clubs"))
    implementation(project(":server:deuxzero"))
    implementation(project(":server:calendrier"))
    implementation(project(":shared-contracts"))
    implementation(libs.spring.boot.starter.data.jpa)
    runtimeOnly(libs.postgresql)
}
