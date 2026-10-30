plugins {
    alias(libs.plugins.conventionFeature)
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            api(project(":feature:matchendirect:domain"))
            api(project(":feature:matchendirect:data"))
            api(project(":feature:matchendirect:presentation"))
        }
    }
}
