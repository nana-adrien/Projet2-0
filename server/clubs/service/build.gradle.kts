plugins {
    id("convention.spring.service")
    alias(libs.plugins.springDependencyManagement)
}

dependencies {
    implementation(project(":server:common"))
    implementation(project(":server:clubs:infra"))
    // ClubService.requestJoinByCode résout l'adhésion via UserRepository (pseudo, existence) :
    // seul le repository est nécessaire, jamais le :service de la feature user.
    implementation(project(":server:user:infra"))
    implementation(project(":shared-contracts"))
}
