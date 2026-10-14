plugins {
    alias(libs.plugins.conventionFeature)
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            api(project(":feature:dashboard:presentation"))
            implementation(project(":feature:calendrier:domain"))
            implementation(project(":feature:deuxzero:domain"))
        }
    }
}
