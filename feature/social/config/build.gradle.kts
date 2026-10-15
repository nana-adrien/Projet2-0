plugins {
    alias(libs.plugins.conventionFeature)
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            api(project(":feature:social:domain"))
            api(project(":feature:social:data"))
            api(project(":feature:social:presentation"))
        }
    }
}
