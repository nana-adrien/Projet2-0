plugins {
    alias(libs.plugins.conventionFeature)
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            api(project(":client:kmp:common:feature:settings:domain"))
            api(project(":client:kmp:common:feature:settings:data"))
            api(project(":client:kmp:common:feature:settings:presentation"))
        }
    }
}
