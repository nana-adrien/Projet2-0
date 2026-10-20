plugins {
    id("convention.spring.service")
    alias(libs.plugins.springDependencyManagement)
    alias(libs.plugins.kotlinJpa)
}

dependencies {
    api(libs.spring.boot.starter.data.jpa)
    // AdhesionClubEntity.utilisateur est une relation @ManyToOne vers UserEntity : exposé en api
    // pour que tout module dépendant de :clubs:infra puisse naviguer cette relation sans déclarer
    // :user:infra lui-même.
    api(project(":server:user:infra"))
}
