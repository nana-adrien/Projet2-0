plugins {
    id("convention.spring.service")
    alias(libs.plugins.springDependencyManagement)
}

dependencies {
    implementation(project(":server:common"))
    implementation(project(":server:calendrier:service"))
    implementation(project(":client-serveur-contrat"))
    implementation(libs.swagger.annotations.jakarta)
}
