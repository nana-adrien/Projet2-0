plugins {
    id("convention.spring.service")
    alias(libs.plugins.springDependencyManagement)
}

dependencies {
    // CU18 — MatchEnDirectWebSocketHandler consomme EvenementService/MatchEnDirectBroadcaster.
    implementation(project(":server:calendrier:service"))
    // MatchEnDirectWebSocketHandler référence MatchEnDirectDto (retour de EvenementService).
    implementation(project(":shared-contracts"))
    implementation(libs.spring.boot.starter.websocket)
}
