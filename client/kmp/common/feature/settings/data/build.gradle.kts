plugins {
    alias(libs.plugins.conventionKmpLibrary)
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(project(":client:kmp:common:core:domain"))
            api(project(":client:kmp:common:core:data"))
            implementation(project(":client:kmp:common:feature:settings:domain"))
            implementation(libs.bundles.dataStore.common)
            implementation(projects.sharedContracts)

        }

        androidMain.dependencies {
            implementation(libs.bundles.dataStore.android)
        }
    }
}
