plugins {
    id("convention.spring.service")
    alias(libs.plugins.springDependencyManagement)
    alias(libs.plugins.kotlinJpa)
}

dependencies {
    api(libs.spring.boot.starter.data.jpa)
    // SignalementEntity.auteur est une relation @ManyToOne vers UserEntity (le signaleur) :
    // exposé en api pour que tout module dépendant de :social:infra la navigue sans déclarer
    // :user:infra lui-même. NB : contenuId reste un UUID brut — référence polymorphe (club, match,
    // profil utilisateur selon contenuType), impossible à typer en relation JPA unique (cf. KDoc
    // de l'entité, lacune UML CU27 documentée dans uml/tracabilite.json).
    api(project(":server:user:infra"))
}
