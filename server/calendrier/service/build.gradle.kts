plugins {
    id("convention.spring.service")
    alias(libs.plugins.springDependencyManagement)
}

dependencies {
    implementation(project(":server:common"))
    implementation(project(":server:calendrier:infra"))
    // CalendrierService/EvenementService/ConvocationService/ValidationService... lisent
    // Camp/DeuxZero/Saison/EffectifEngageRepository (deuxzero), NotificationEntity/Repository
    // (notifications) et UserRepository (user) — uniquement leurs :infra, jamais leurs :service.
    // NB : la feature clubs n'est PAS utilisée ici malgré l'ancienne dépendance au module
    // monolithique ":server:clubs" (aucun import clubs.* dans le code, vérifié par grep) —
    // dépendance supprimée par ce refactor.
    implementation(project(":server:deuxzero:infra"))
    implementation(project(":server:notifications:infra"))
    implementation(project(":server:user:infra"))
    implementation(project(":shared-contracts"))
    // MatchEnDirectBroadcaster (CU18) utilise WebSocketSession/TextMessage directement : c'est
    // un @Service appelé par EvenementService (CU17), pas seulement par le handler de :websocket.
    implementation(libs.spring.boot.starter.websocket)
}
