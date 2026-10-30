plugins {
    alias(libs.plugins.conventionKmpLibrary)
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(project(":client:kmp:common:core:domain"))
            api(project(":client:kmp:common:core:data"))
            implementation(project(":client:kmp:common:feature:validation:domain"))
            implementation(project(":client:kmp:common:feature:calendrier:data"))
            implementation(project(":client:kmp:common:feature:matchendirect:data"))
            implementation(projects.sharedContracts)
            implementation(libs.kotlinx.serialization.json)
        }
    }
}
