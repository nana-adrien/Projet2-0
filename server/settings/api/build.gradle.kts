plugins {
    id("convention.spring.service")
    alias(libs.plugins.springDependencyManagement)
}

dependencies {
    implementation(project(":server:common"))
    implementation(project(":server:settings:service"))
    implementation(project(":shared-contracts"))
}
