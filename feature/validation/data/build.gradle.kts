plugins {
    alias(libs.plugins.conventionKmpLibrary)
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(project(":core:domain"))
            api(project(":core:data"))
            implementation(project(":feature:validation:domain"))
            implementation(project(":feature:calendrier:data"))
            implementation(project(":feature:matchendirect:data"))
            implementation(projects.sharedContracts)
            implementation(libs.kotlinx.serialization.json)
        }
    }
}
