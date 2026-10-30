package empire.digiprem.projet2_0.core.design_system.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf

private val LightColorScheme = lightColorScheme(
    primary          = PDZColor.Gazon,
    onPrimary        = PDZColor.Blanc,
    secondary        = PDZColor.Ligne,
    onSecondary      = PDZColor.Bitume,
    background       = PDZColor.Craie,
    onBackground     = PDZColor.Bitume,
    surface          = PDZColor.Blanc,
    onSurface        = PDZColor.Bitume,
    surfaceVariant   = PDZColor.Gazon50,
    onSurfaceVariant = PDZColor.Gris600,
    outline          = PDZColor.Gris300,
    outlineVariant   = PDZColor.Gris200,
    error            = PDZColor.Erreur,
    onError          = PDZColor.Blanc,
)

private val DarkColorScheme = darkColorScheme(
    primary          = PDZColor.GazonSombre,
    onPrimary        = PDZColor.Bitume,
    secondary        = PDZColor.Ligne,
    onSecondary      = PDZColor.Bitume,
    background       = PDZColor.CraieSombre,
    onBackground     = PDZColor.BitumeSombre,
    surface          = PDZColor.BlancSombre,
    onSurface        = PDZColor.BitumeSombre,
    surfaceVariant   = PDZColor.Gazon50Sombre,
    onSurfaceVariant = PDZColor.Gris600Sombre,
    outline          = PDZColor.Gris600Sombre,
    outlineVariant   = PDZColor.Gris200Sombre,
    error            = PDZColor.Erreur,
    onError          = PDZColor.Blanc,
)

val LocalColorScheme = staticCompositionLocalOf<ColorScheme> {
    error("No ColorScheme provided")
}

@Composable
fun PDZTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit,
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    CompositionLocalProvider(LocalColorScheme provides colorScheme) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography  = AppTypography(),
            content     = content,
        )
    }
}
