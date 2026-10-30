plugins {
    alias(libs.plugins.conventionKmpLibrary)
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(project(":client:kmp:common:core:domain"))
            api(project(":client:kmp:common:core:data"))
            implementation(project(":client:kmp:common:feature:clubs:domain"))
            implementation(projects.sharedContracts)
            implementation(libs.kotlinx.serialization.json)
        }
    }
}
