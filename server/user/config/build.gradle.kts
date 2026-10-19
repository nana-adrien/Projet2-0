plugins {
    id("convention.spring.service")
    alias(libs.plugins.springDependencyManagement)
}

// Module façade : server:app ne dépend que de :config, jamais directement de :api/:service/:infra.
dependencies {
    api(project(":server:user:api"))
}
