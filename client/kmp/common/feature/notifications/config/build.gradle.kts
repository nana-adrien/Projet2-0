plugins {
    alias(libs.plugins.conventionFeature)
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            api(project(":client:kmp:common:feature:notifications:domain"))
            api(project(":client:kmp:common:feature:notifications:data"))
            api(project(":client:kmp:common:feature:notifications:presentation"))
            implementation(project(":client:kmp:common:feature:deuxzero:domain"))
        }
    }
}
