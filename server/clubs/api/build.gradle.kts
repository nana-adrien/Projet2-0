plugins {
    id("convention.spring.service")
    alias(libs.plugins.springDependencyManagement)
}

dependencies {
    implementation(project(":server:common"))
    implementation(project(":server:clubs:service"))
    implementation(project(":shared-contracts"))
    // ClubController utilise @Tag (io.swagger.v3.oas.annotations) directement.
    implementation(libs.spring.springdoc.openapi)
}
