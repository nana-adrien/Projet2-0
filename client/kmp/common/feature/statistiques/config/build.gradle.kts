plugins {
    alias(libs.plugins.conventionFeature)
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            api(project(":client:kmp:common:feature:statistiques:domain"))
            api(project(":client:kmp:common:feature:statistiques:data"))
            api(project(":client:kmp:common:feature:statistiques:presentation"))
        }
    }
}
