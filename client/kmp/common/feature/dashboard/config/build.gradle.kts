plugins {
    alias(libs.plugins.conventionFeature)
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            api(project(":client:kmp:common:feature:dashboard:presentation"))
            implementation(project(":client:kmp:common:feature:calendrier:domain"))
            implementation(project(":client:kmp:common:feature:deuxzero:domain"))
        }
    }
}
