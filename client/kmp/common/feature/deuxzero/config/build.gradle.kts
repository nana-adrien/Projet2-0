plugins {
    alias(libs.plugins.conventionFeature)
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            api(project(":client:kmp:common:feature:deuxzero:domain"))
            api(project(":client:kmp:common:feature:deuxzero:data"))
            api(project(":client:kmp:common:feature:deuxzero:presentation"))
        }
    }
}
