plugins {
    alias(libs.plugins.conventionFeature)
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            api(project(":feature:calendrier:domain"))
            api(project(":feature:calendrier:data"))
            api(project(":feature:calendrier:presentation"))
        }
    }
}
