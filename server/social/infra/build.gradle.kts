plugins {
    id("convention.spring.service")
    alias(libs.plugins.springDependencyManagement)
    alias(libs.plugins.kotlinJpa)
}

dependencies {
    api(libs.spring.boot.starter.data.jpa)
}
