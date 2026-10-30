plugins {
    alias(libs.plugins.conventionFeature)
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(project(":client:kmp:common:core:domain"))
            api(project(":client:kmp:common:core:presentation"))
            implementation(project(":client:kmp:common:core:design_system"))
            // Dashboard needs access to all inner feature screens
            implementation(project(":client:kmp:common:feature:profile:presentation"))
            implementation(project(":client:kmp:common:feature:notifications:presentation"))
            implementation(project(":client:kmp:common:feature:settings:presentation"))
            implementation(project(":client:kmp:common:feature:clubs:domain"))
            implementation(project(":client:kmp:common:feature:clubs:presentation"))
            implementation(project(":client:kmp:common:feature:deuxzero:domain"))
            implementation(project(":client:kmp:common:feature:deuxzero:presentation"))
            implementation(project(":client:kmp:common:feature:calendrier:domain"))
            implementation(project(":client:kmp:common:feature:calendrier:presentation"))
            implementation(project(":client:kmp:common:feature:matchendirect:domain"))
            implementation(project(":client:kmp:common:feature:matchendirect:presentation"))
            implementation(project(":client:kmp:common:feature:validation:domain"))
            implementation(project(":client:kmp:common:feature:validation:presentation"))
            implementation(project(":client:kmp:common:feature:statistiques:domain"))
            implementation(project(":client:kmp:common:feature:statistiques:presentation"))
            implementation(project(":client:kmp:common:feature:amical:domain"))
            implementation(project(":client:kmp:common:feature:amical:presentation"))
        }
    }
}
