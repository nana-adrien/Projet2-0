package extension

import org.gradle.api.Project
import java.util.Locale

data class KmpTargets(
    val android: Boolean,
    val ios: Boolean,
    val desktop: Boolean,
    val web: Boolean,
)

val Project.kmpTargets: KmpTargets
    get() = KmpTargets(
        android = true,
        ios     = true,
        desktop = false,
        web     = false,
    )

fun Project.relativePackageName(): String {
    // '-' n'est pas un caractère valide dans un identifiant Java/Kotlin (ex. ":shared-contracts").
    return path.replace(':', '.').replace('-', '_').lowercase()
}

fun Project.pathToPackageName(): String {
    val base = "empire.digiprem.projet2_0"
    return "$base${relativePackageName()}"
}

fun Project.pathToResourcePrefix(): String {
    return path.replace(':', '_').lowercase().drop(1) + "_"
}

fun Project.pathToFrameworkName(): String {
    val parts = path.split(":", "_", "-", " ")
    return parts.joinToString("") { part ->
        part.replaceFirstChar { it.titlecase(Locale.ROOT) }
    }
}