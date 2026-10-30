plugins {
    alias(libs.plugins.conventionFeature)
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            api(project(":client:kmp:common:feature:calendrier:domain"))
            api(project(":client:kmp:common:feature:calendrier:data"))
            api(project(":client:kmp:common:feature:calendrier:presentation"))
            implementation(project(":client:kmp:common:feature:deuxzero:domain"))
        }
    }
}
