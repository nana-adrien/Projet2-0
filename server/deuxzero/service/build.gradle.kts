plugins {
    id("convention.spring.service")
    alias(libs.plugins.springDependencyManagement)
}

dependencies {
    implementation(project(":server:common"))
    implementation(project(":server:deuxzero:infra"))
    // DeuxZeroService/EffectifService lisent AdhesionClubRepository/ClubRepository et
    // UserRepository — seuls les repositories sont nécessaires, jamais les :service.
    implementation(project(":server:clubs:infra"))
    implementation(project(":server:user:infra"))
    implementation(project(":shared-contracts"))
}
