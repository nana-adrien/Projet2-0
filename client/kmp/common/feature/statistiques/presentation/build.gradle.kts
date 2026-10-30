plugins {
    alias(libs.plugins.conventionFeature)
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(project(":client:kmp:common:core:domain"))
            api(project(":client:kmp:common:core:presentation"))
            implementation(project(":client:kmp:common:core:design_system"))
            implementation(project(":client:kmp:common:feature:statistiques:domain"))
            implementation(project(":client:kmp:common:feature:deuxzero:domain"))
            implementation(project(":client:kmp:common:feature:deuxzero:presentation"))
            implementation(project(":client:kmp:common:feature:social:domain"))
            implementation(project(":client:kmp:common:feature:social:presentation"))
        }
    }
}
