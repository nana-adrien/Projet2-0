plugins {
    id("convention.spring.service")
    alias(libs.plugins.springDependencyManagement)
    alias(libs.plugins.kotlinJpa)
}

dependencies {
    api(libs.spring.boot.starter.data.jpa)
    // Match/Composition/Convocation/Evenement/Validation portent des relations @ManyToOne vers
    // SaisonEntity/CampEntity (deuxzero), ClubEntity (clubs) et UserEntity (user) : exposé en api
    // pour que tout module dépendant de :calendrier:infra navigue ces relations sans déclarer ces
    // modules lui-même.
    api(project(":server:user:infra"))
    api(project(":server:clubs:infra"))
    api(project(":server:deuxzero:infra"))
}
