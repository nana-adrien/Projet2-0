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
            implementation(project(":core:config"))
            implementation(project(":core:design_system"))
            implementation(project(":core:presentation"))

            // Feature config modules (pull in domain/data/presentation transitively)
            implementation(project(":feature:auth:config"))
            implementation(project(":feature:profile:config"))
            implementation(project(":feature:notifications:config"))
            implementation(project(":feature:settings:config"))
            implementation(project(":feature:clubs:config"))
            implementation(project(":feature:deuxzero:config"))
            implementation(project(":feature:calendrier:config"))
            implementation(project(":feature:matchendirect:config"))
            implementation(project(":feature:validation:config"))
            implementation(project(":feature:statistiques:config"))
            implementation(project(":feature:amical:config"))
            implementation(project(":feature:social:config"))
            implementation(project(":feature:dashboard:config"))
        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }
    }
}

dependencies {
    androidRuntimeClasspath(libs.compose.uiTooling)
}
