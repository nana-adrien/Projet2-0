plugins {
    alias(libs.plugins.conventionFeature)
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(project(":core:domain"))
            api(project(":core:presentation"))
            implementation(project(":core:design_system"))
            implementation(project(":feature:notifications:domain"))
            implementation(project(":feature:deuxzero:domain"))
            implementation(project(":feature:deuxzero:presentation"))
        }
    }
}
