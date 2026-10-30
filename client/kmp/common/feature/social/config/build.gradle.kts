plugins {
    alias(libs.plugins.conventionFeature)
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            api(project(":client:kmp:common:feature:social:domain"))
            api(project(":client:kmp:common:feature:social:data"))
            api(project(":client:kmp:common:feature:social:presentation"))
        }
    }
}
