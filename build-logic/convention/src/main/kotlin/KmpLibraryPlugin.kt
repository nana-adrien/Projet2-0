import convention.configureKotlin
import convention.configureKotlinMultiplatform
import extension.commonMainImplementation
import extension.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

class KmpLibraryPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("com.android.kotlin.multiplatform.library")
                apply("org.jetbrains.kotlin.multiplatform")
                apply("org.jetbrains.kotlin.plugin.serialization")
            }
            plugins.withId("com.android.kotlin.multiplatform.library") {
                configureKotlinMultiplatform()
            }
            configureKotlin()
            dependencies {
                commonMainImplementation(libs.findLibrary("kotlinx-coroutines-core").get())
                commonMainImplementation(libs.findLibrary("kermit").get())
                "commonTestImplementation"(libs.findLibrary("kotlin-test").get())
            }
        }
    }
}