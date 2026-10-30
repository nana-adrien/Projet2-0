plugins {
    alias(libs.plugins.conventionFeature)
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            api(project(":client:kmp:common:feature:profile:domain"))
            api(project(":client:kmp:common:feature:profile:data"))
            api(project(":client:kmp:common:feature:profile:presentation"))
        }
    }
}
