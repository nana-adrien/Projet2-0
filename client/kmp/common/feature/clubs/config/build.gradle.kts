plugins {
    alias(libs.plugins.conventionFeature)
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            api(project(":client:kmp:common:feature:clubs:domain"))
            api(project(":client:kmp:common:feature:clubs:data"))
            api(project(":client:kmp:common:feature:clubs:presentation"))
        }
    }
}
