plugins {
    alias(libs.plugins.conventionKmpLibrary)
}

kotlin {
    jvm()

    sourceSets {
        commonMain.dependencies {
            implementation(libs.kotlinx.serialization.json)
            api(libs.konform)
        }
    }
}