plugins {
    id("convention.spring.service")
    alias(libs.plugins.springDependencyManagement)
    alias(libs.plugins.kotlinJpa)
}

dependencies {
    api(libs.spring.boot.starter.data.jpa)
    // Camp/DeuxZero/EffectifEngage/Saison portent désormais des relations @ManyToOne vers
    // UserEntity (capitaine, viceCapitaine, organisateur, utilisateur) et ClubEntity (club) :
    // exposé en api pour que tout module dépendant de :deuxzero:infra navigue ces relations
    // sans déclarer :user:infra/:clubs:infra lui-même.
    api(project(":server:user:infra"))
    api(project(":server:clubs:infra"))
}
