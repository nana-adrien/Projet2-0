plugins {
    alias(libs.plugins.conventionFeature)
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            api(project(":feature:validation:domain"))
            api(project(":feature:validation:data"))
            api(project(":feature:validation:presentation"))
        }
    }
}
