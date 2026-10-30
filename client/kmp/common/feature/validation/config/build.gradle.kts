plugins {
    alias(libs.plugins.conventionFeature)
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            api(project(":client:kmp:common:feature:validation:domain"))
            api(project(":client:kmp:common:feature:validation:data"))
            api(project(":client:kmp:common:feature:validation:presentation"))
        }
    }
}
