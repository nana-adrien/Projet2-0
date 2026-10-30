plugins {
    alias(libs.plugins.conventionFeature)
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            api(project(":client:kmp:common:feature:matchendirect:domain"))
            api(project(":client:kmp:common:feature:matchendirect:data"))
            api(project(":client:kmp:common:feature:matchendirect:presentation"))
        }
    }
}
