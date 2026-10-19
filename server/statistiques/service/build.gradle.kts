plugins {
    id("convention.spring.service")
    alias(libs.plugins.springDependencyManagement)
}

dependencies {
    implementation(project(":server:common"))
    implementation(project(":server:statistiques:infra"))
    // StatistiquesService/VoteService/ProfilProgressionService/BilanService lisent Match,
    // Composition, Evenement (calendrier), Camp, Saison (deuxzero), Club (clubs) et User (user) —
    // uniquement leurs :infra respectifs.
    implementation(project(":server:calendrier:infra"))
    implementation(project(":server:deuxzero:infra"))
    implementation(project(":server:clubs:infra"))
    implementation(project(":server:user:infra"))
    // Exception documentée : BilanService.cloturerEtBilan délègue à
    // SaisonService.closeSaison (vérification organisateur + RG-18 saison déjà clôturée) pour
    // ne pas dupliquer cette règle métier — seul cas où statistiques dépend d'un :service
    // externe plutôt que du :infra correspondant.
    implementation(project(":server:deuxzero:service"))
    implementation(project(":shared-contracts"))
}
