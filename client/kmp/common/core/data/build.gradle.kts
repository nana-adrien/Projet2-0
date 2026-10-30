plugins {
    alias(libs.plugins.conventionKmpLibrary)
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            api(project(":core:domain"))
            // Contrats partagés avec le serveur (DTO login/refresh) — évite de dupliquer
            // le format exact de /v1/auth/refresh, déjà utilisé côté server/.
            implementation(project(":shared-contracts"))
            implementation(libs.kotlinx.serialization.json)
            // Ktor — api so feature:data modules can inject HttpClient
            api(libs.ktor.client.core)
            api(libs.ktor.client.content.neg)
            api(libs.ktor.serialization.json)
            api(libs.ktor.client.logging)
            api(libs.ktor.client.auth)
            api(libs.ktor.client.websockets)
            // DataStore — api so core:config can access DataStore types
            api(libs.bundles.dataStore.common)
        }

        androidMain.dependencies {
            api(libs.bundles.dataStore.android)
            implementation(libs.ktor.client.okhttp)
        }
        iosMain.dependencies {
            implementation(libs.ktor.client.darwin)
        }
    }
}