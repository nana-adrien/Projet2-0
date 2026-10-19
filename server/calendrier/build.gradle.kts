plugins {
    id("convention.spring.service")
    alias(libs.plugins.springDependencyManagement)
    alias(libs.plugins.kotlinJpa)
}

dependencies {
    implementation(project(":server:common"))
    implementation(project(":server:user:infra"))
    implementation(project(":server:clubs"))
    implementation(project(":server:deuxzero"))
    implementation(project(":server:notifications"))
    implementation(project(":shared-contracts"))
    implementation(libs.spring.boot.starter.data.jpa)
    implementation(libs.spring.boot.starter.websocket)
    runtimeOnly(libs.postgresql)
}
