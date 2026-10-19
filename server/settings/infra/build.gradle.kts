plugins {
    id("convention.spring.service")
    alias(libs.plugins.springDependencyManagement)
    alias(libs.plugins.kotlinJpa)
}

dependencies {
    // api : les repositories implémentent JpaRepository, nécessaire au classpath de compilation
    // de tout module qui dépend de :settings:infra.
    api(libs.spring.boot.starter.data.jpa)
}
