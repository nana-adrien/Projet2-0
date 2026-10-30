plugins {
    alias(libs.plugins.conventionFeature)
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            api(project(":client:kmp:common:feature:auth:domain"))
            api(project(":client:kmp:common:feature:auth:data"))
            api(project(":client:kmp:common:feature:auth:presentation"))
        }
    }
}
