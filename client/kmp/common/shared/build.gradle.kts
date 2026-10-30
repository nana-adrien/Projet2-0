import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.conventionApp)
}

kotlin {

    sourceSets {
        androidMain.dependencies {
            implementation(libs.koin.android)
        }
        commonMain.dependencies {
            implementation(libs.compose.runtime)
            implementation(libs.compose.foundation)
            implementation(libs.compose.material3)
            implementation(libs.compose.ui)
            implementation(libs.compose.components.resources)
            implementation(libs.compose.uiToolingPreview)
            implementation(libs.androidx.lifecycle.viewmodelCompose)
            implementation(libs.androidx.lifecycle.runtimeCompose)

            // Navigation
            implementation(libs.navigation.compose)

            // Koin
            implementation(libs.koin.core)
            implementation(libs.koin.compose)
            implementation(libs.koin.compose.viewmodel)

            // Core modules
            implementation(project(":client:kmp:common:core:config"))
            implementation(project(":client:kmp:common:core:design_system"))
            implementation(project(":client:kmp:common:core:presentation"))

            // Feature config modules (pull in domain/data/presentation transitively)
            implementation(project(":client:kmp:common:feature:auth:config"))
            implementation(project(":client:kmp:common:feature:profile:config"))
            implementation(project(":client:kmp:common:feature:notifications:config"))
            implementation(project(":client:kmp:common:feature:settings:config"))
            implementation(project(":client:kmp:common:feature:clubs:config"))
            implementation(project(":client:kmp:common:feature:deuxzero:config"))
            implementation(project(":client:kmp:common:feature:calendrier:config"))
            implementation(project(":client:kmp:common:feature:matchendirect:config"))
            implementation(project(":client:kmp:common:feature:validation:config"))
            implementation(project(":client:kmp:common:feature:statistiques:config"))
            implementation(project(":client:kmp:common:feature:amical:config"))
            implementation(project(":client:kmp:common:feature:social:config"))
            implementation(project(":client:kmp:common:feature:dashboard:config"))
        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }
    }
}

dependencies {
    androidRuntimeClasspath(libs.compose.uiTooling)
}
