package empire.digiprem.projet2_0.core.design_system.theme

import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import empire.digiprem.projet2_0.core.design_system.generated.resources.Res
import empire.digiprem.projet2_0.core.design_system.generated.resources.barlow_condensed_bold
import empire.digiprem.projet2_0.core.design_system.generated.resources.barlow_condensed_semibold
import empire.digiprem.projet2_0.core.design_system.generated.resources.barlow_medium
import empire.digiprem.projet2_0.core.design_system.generated.resources.barlow_regular
import empire.digiprem.projet2_0.core.design_system.generated.resources.barlow_semibold
import org.jetbrains.compose.resources.Font

private const val TabularNumsFeature = "tnum"

@Composable
private fun barlowCondensedFontFamily(): FontFamily = FontFamily(
    Font(resource = Res.font.barlow_condensed_semibold, weight = FontWeight.SemiBold),
    Font(resource = Res.font.barlow_condensed_bold, weight = FontWeight.Bold),
)

@Composable
private fun barlowFontFamily(): FontFamily = FontFamily(
    Font(resource = Res.font.barlow_regular, weight = FontWeight.Normal),
    Font(resource = Res.font.barlow_medium, weight = FontWeight.Medium),
    Font(resource = Res.font.barlow_semibold, weight = FontWeight.SemiBold),
)

object PDZTypography {
    val score: TextStyle
        @Composable get() = TextStyle(
            fontFamily = barlowCondensedFontFamily(),
            fontWeight = FontWeight.Bold,
            fontSize = 56.sp,
            lineHeight = 56.sp,
            fontFeatureSettings = TabularNumsFeature,
        )

    val chrono: TextStyle
        @Composable get() = TextStyle(
            fontFamily = barlowCondensedFontFamily(),
            fontWeight = FontWeight.Bold,
            fontSize = 72.sp,
            lineHeight = 72.sp,
            fontFeatureSettings = TabularNumsFeature,
        )

    val display: TextStyle
        @Composable get() = TextStyle(
            fontFamily = barlowCondensedFontFamily(),
            fontWeight = FontWeight.Bold,
            fontSize = 32.sp,
            lineHeight = 36.sp,
        )

    val titre1: TextStyle
        @Composable get() = TextStyle(
            fontFamily = barlowCondensedFontFamily(),
            fontWeight = FontWeight.Bold,
            fontSize = 24.sp,
            lineHeight = 28.sp,
        )

    val titre2: TextStyle
        @Composable get() = TextStyle(
            fontFamily = barlowFontFamily(),
            fontWeight = FontWeight.SemiBold,
            fontSize = 18.sp,
            lineHeight = 24.sp,
        )

    val corps: TextStyle
        @Composable get() = TextStyle(
            fontFamily = barlowFontFamily(),
            fontWeight = FontWeight.Normal,
            fontSize = 16.sp,
            lineHeight = 24.sp,
        )

    val corpsS: TextStyle
        @Composable get() = TextStyle(
            fontFamily = barlowFontFamily(),
            fontWeight = FontWeight.Normal,
            fontSize = 14.sp,
            lineHeight = 20.sp,
        )

    val legende: TextStyle
        @Composable get() = TextStyle(
            fontFamily = barlowFontFamily(),
            fontWeight = FontWeight.Medium,
            fontSize = 12.sp,
            lineHeight = 16.sp,
        )

    val chiffreStat: TextStyle
        @Composable get() = TextStyle(
            fontFamily = barlowCondensedFontFamily(),
            fontWeight = FontWeight.SemiBold,
            fontSize = 20.sp,
            lineHeight = 24.sp,
            fontFeatureSettings = TabularNumsFeature,
        )

    val chiffreStatTuile: TextStyle
        @Composable get() = TextStyle(
            fontFamily = barlowCondensedFontFamily(),
            fontWeight = FontWeight.SemiBold,
            fontSize = 26.sp,
            lineHeight = 28.sp,
            fontFeatureSettings = TabularNumsFeature,
        )

    val bouton: TextStyle
        @Composable get() = TextStyle(
            fontFamily = barlowFontFamily(),
            fontWeight = FontWeight.SemiBold,
            fontSize = 16.sp,
            lineHeight = 20.sp,
        )

    val boutonPetit: TextStyle
        @Composable get() = TextStyle(
            fontFamily = barlowFontFamily(),
            fontWeight = FontWeight.SemiBold,
            fontSize = 14.sp,
            lineHeight = 18.sp,
        )

    val onglet: TextStyle
        @Composable get() = TextStyle(
            fontFamily = barlowFontFamily(),
            fontWeight = FontWeight.SemiBold,
            fontSize = 11.sp,
            lineHeight = 14.sp,
        )
}

@Composable
fun AppTypography() = Typography(
    displayLarge = PDZTypography.score,
    displayMedium = PDZTypography.chrono,
    displaySmall = PDZTypography.display,
    headlineLarge = PDZTypography.display,
    headlineMedium = PDZTypography.titre1,
    headlineSmall = PDZTypography.titre1,
    titleLarge = PDZTypography.titre1,
    titleMedium = PDZTypography.titre2,
    titleSmall = PDZTypography.titre2,
    bodyLarge = PDZTypography.corps,
    bodyMedium = PDZTypography.corpsS,
    bodySmall = PDZTypography.legende,
    labelLarge = PDZTypography.bouton,
    labelMedium = PDZTypography.boutonPetit,
    labelSmall = PDZTypography.onglet,
)
