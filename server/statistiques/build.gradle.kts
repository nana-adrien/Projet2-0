plugins {
    id("convention.spring.service")
    alias(libs.plugins.springDependencyManagement)
    alias(libs.plugins.kotlinJpa)
}

dependencies {
    implementation(project(":server:common"))
    implementation(project(":server:user:infra"))
    implementation(project(":server:clubs:infra"))
    implementation(project(":server:deuxzero:infra"))
    // BilanService.cloturerEtBilan réutilise SaisonService.closeSaison (vérif. organisateur +
    // saison déjà clôturée, RG-18) pour ne pas dupliquer cette règle métier — exception
    // documentée, seul cas où statistiques dépend d'un :service externe plutôt qu'un :infra.
    implementation(project(":server:deuxzero:service"))
    implementation(project(":server:calendrier"))
    implementation(project(":shared-contracts"))
    implementation(libs.spring.boot.starter.data.jpa)
    runtimeOnly(libs.postgresql)
}
