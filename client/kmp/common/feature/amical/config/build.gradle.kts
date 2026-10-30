plugins {
    alias(libs.plugins.conventionFeature)
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            api(project(":client:kmp:common:feature:amical:domain"))
            api(project(":client:kmp:common:feature:amical:data"))
            api(project(":client:kmp:common:feature:amical:presentation"))
        }
    }
}
