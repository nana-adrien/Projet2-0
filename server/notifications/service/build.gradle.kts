plugins {
    id("convention.spring.service")
    alias(libs.plugins.springDependencyManagement)
}

dependencies {
    implementation(project(":server:common"))
    implementation(project(":server:notifications:infra"))
    // PreferenceNotificationService lit CampRepository/DeuxZeroRepository/EffectifEngageRepository
    // pour résoudre les destinataires — seul :deuxzero:infra est nécessaire.
    implementation(project(":server:deuxzero:infra"))
    implementation(project(":shared-contracts"))
    implementation("org.springframework.boot:spring-boot-starter-mail")
    // Pour gérer les templates HTML
    implementation("org.springframework.boot:spring-boot-starter-thymeleaf")
    implementation("com.google.firebase:firebase-admin:9.3.0")
    implementation(projects.server.user.infra)
}
