plugins {
    id("convention.spring.service")
    alias(libs.plugins.springDependencyManagement)
    alias(libs.plugins.kotlinJpa)
}

dependencies {
    api(libs.spring.boot.starter.data.jpa)
    // Vote.match/votant/candidat et BadgeObtenu.utilisateur sont des relations @ManyToOne :
    // exposé en api pour que tout module dépendant de :statistiques:infra les navigue sans
    // déclarer :user:infra/:calendrier:infra lui-même.
    api(project(":server:user:infra"))
    api(project(":server:calendrier:infra"))
}
