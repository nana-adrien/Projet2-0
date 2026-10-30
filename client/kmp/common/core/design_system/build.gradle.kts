plugins {
    alias(libs.plugins.conventionCmpLibrary)
}

compose.resources {
    publicResClass = true
    generateResClass = always
    packageOfResClass = "empire.digiprem.projet2_0.core.design_system.generated.resources"
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            api(project(":client:kmp:common:core:domain"))
            api(project(":client:kmp:common:core:presentation"))
            implementation(libs.coil.compose)
            implementation(libs.coil.network)
            implementation(libs.compose.materialIconsCore)
        }
    }
}
