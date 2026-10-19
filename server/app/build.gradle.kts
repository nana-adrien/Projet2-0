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
    implementation(project(":server:user:config"))
    implementation(project(":server:settings:config"))
    implementation(project(":server:notifications"))
    implementation(project(":server:clubs:config"))
    implementation(project(":server:deuxzero"))
    implementation(project(":server:calendrier"))
    implementation(project(":server:statistiques"))
    implementation(project(":server:amical"))
    implementation(project(":server:social:config"))
    implementation(libs.spring.boot.starter.data.jpa)
    //implementation(libs.spring.springdoc.openapi)

    implementation(libs.spring.springdoc.openapi)
    implementation(libs.flyway.core)
    runtimeOnly(libs.flyway.postgresql)
    runtimeOnly(libs.postgresql)
    annotationProcessor(libs.spring.boot.processor)
}
