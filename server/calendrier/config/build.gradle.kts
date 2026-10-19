plugins {
    id("convention.spring.service")
    alias(libs.plugins.springDependencyManagement)
}

dependencies {
    api(project(":server:calendrier:api"))
    // CU18 — le handler WebSocket doit être sur le classpath de server:app sans que celui-ci
    // n'ait à connaître son existence.
    api(project(":server:calendrier:websocket"))
}
