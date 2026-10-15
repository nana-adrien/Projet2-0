package convention

import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import extension.pathToFrameworkName

internal fun Project.configureKotlinMultiplatform() {
    configureAndroidTarget()
    configureIosTarget(baseName = pathToFrameworkName())

    extensions.configure<KotlinMultiplatformExtension> {
        compilerOptions {
            freeCompilerArgs.add("-Xexpect-actual-classes")
            freeCompilerArgs.add("-opt-in=kotlin.RequiresOptIn")
        }
    }
}