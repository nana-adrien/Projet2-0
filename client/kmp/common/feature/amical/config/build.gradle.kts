plugins {
    alias(libs.plugins.conventionFeature)
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            api(project(":feature:amical:domain"))
            api(project(":feature:amical:data"))
            api(project(":feature:amical:presentation"))
        }
    }
}
