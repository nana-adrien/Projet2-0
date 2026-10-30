plugins {
    alias(libs.plugins.conventionFeature)
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            api(project(":feature:deuxzero:domain"))
            api(project(":feature:deuxzero:data"))
            api(project(":feature:deuxzero:presentation"))
        }
    }
}
