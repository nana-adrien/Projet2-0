plugins {
    id("convention.spring.service")
    alias(libs.plugins.springDependencyManagement)
}

// La feature amical n'a aucune entité propre (CU21 réutilise MatchEntity de calendrier) : pas de
// module :amical:infra. AmicalService ne lit que MatchRepository (calendrier) et
// AdhesionClubRepository (clubs) — leurs :infra seulement, jamais leurs :service.
dependencies {
    implementation(project(":server:common"))
    implementation(project(":server:calendrier:infra"))
    implementation(project(":server:clubs:infra"))
    implementation(project(":shared-contracts"))
}
