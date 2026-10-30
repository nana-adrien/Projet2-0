plugins {
    alias(libs.plugins.conventionFeature)
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            api(project(":feature:statistiques:domain"))
            api(project(":feature:statistiques:data"))
            api(project(":feature:statistiques:presentation"))
        }
    }
}
