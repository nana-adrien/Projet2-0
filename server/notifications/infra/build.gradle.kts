plugins {
    id("convention.spring.service")
    alias(libs.plugins.springDependencyManagement)
    alias(libs.plugins.kotlinJpa)
}

dependencies {
    api(libs.spring.boot.starter.data.jpa)
    // Notification.user et PreferenceNotification.utilisateur/deuxZero sont des relations
    // @ManyToOne : exposé en api pour que tout module dépendant de :notifications:infra les
    // navigue sans déclarer :user:infra/:deuxzero:infra lui-même.
    api(project(":server:user:infra"))
    api(project(":server:deuxzero:infra"))
}
