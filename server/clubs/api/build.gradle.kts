plugins {
    id("convention.spring.service")
    alias(libs.plugins.springDependencyManagement)
}

dependencies {
    implementation(project(":server:common"))
    implementation(project(":server:clubs:service"))
    implementation(project(":shared-contracts"))
    implementation(libs.swagger.annotations.jakarta)
}
