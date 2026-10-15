plugins {
    alias(libs.plugins.conventionFeature)
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(project(":core:domain"))
            api(project(":core:presentation"))
            implementation(project(":core:design_system"))
            // Dashboard needs access to all inner feature screens
            implementation(project(":feature:profile:presentation"))
            implementation(project(":feature:notifications:presentation"))
            implementation(project(":feature:settings:presentation"))
            implementation(project(":feature:clubs:domain"))
            implementation(project(":feature:clubs:presentation"))
            implementation(project(":feature:deuxzero:domain"))
            implementation(project(":feature:deuxzero:presentation"))
            implementation(project(":feature:calendrier:domain"))
            implementation(project(":feature:calendrier:presentation"))
            implementation(project(":feature:matchendirect:domain"))
            implementation(project(":feature:matchendirect:presentation"))
            implementation(project(":feature:validation:domain"))
            implementation(project(":feature:validation:presentation"))
            implementation(project(":feature:statistiques:domain"))
            implementation(project(":feature:statistiques:presentation"))
        }
    }
}
