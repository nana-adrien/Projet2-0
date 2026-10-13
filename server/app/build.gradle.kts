plugins {
    alias(libs.plugins.springBoot)
    id("convention.spring.service")
    alias(libs.plugins.springDependencyManagement)
}

springBoot {
    mainClass.set("empire.digiprem.projet2_0.server.app.ServerApplicationKt")
}

dependencies {
    implementation(project(":server:common"))
    implementation(project(":server:user"))
    implementation(project(":server:settings"))
    implementation(project(":server:notifications"))
    implementation(libs.spring.boot.starter.data.jpa)
    //implementation(libs.spring.springdoc.openapi)

    implementation(libs.spring.springdoc.openapi)
    implementation(libs.flyway.core)
    runtimeOnly(libs.flyway.postgresql)
    runtimeOnly(libs.postgresql)
    annotationProcessor(libs.spring.boot.processor)
}
