plugins {
    id("convention.spring.service")
    alias(libs.plugins.springDependencyManagement)
    alias(libs.plugins.kotlinJpa)
}

dependencies {
    implementation(project(":server:common"))
    implementation(project(":server:clubs:infra"))
    implementation(project(":server:calendrier"))
    implementation(project(":shared-contracts"))
    implementation(libs.spring.boot.starter.data.jpa)
    implementation(libs.spring.springdoc.openapi)
    runtimeOnly(libs.postgresql)
}
