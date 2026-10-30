package empire.digiprem.projet2_0.feature.auth.welcome

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import empire.digiprem.projet2_0.core.design_system.components.buttons.PDZButton
import empire.digiprem.projet2_0.core.design_system.components.buttons.PDZButtonType
import empire.digiprem.projet2_0.core.design_system.theme.PDZColor
import empire.digiprem.projet2_0.core.design_system.theme.PDZSpacing

/** E01 — Bienvenue (CU1/CU2). Seul écran entièrement vert : installe la marque. */
@Composable
fun WelcomeScreen(
    onCreateAccount: () -> Unit,
    onLogin: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(PDZColor.Gazon)
            .padding(horizontal = PDZSpacing.space6, vertical = PDZSpacing.space8),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Column(
            modifier = Modifier.fillMaxWidth().weight(1f),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = "2-0",
                color = PDZColor.Blanc,
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.displayLarge,
            )
            Spacer(Modifier.height(PDZSpacing.space6))
            Text(
                text = "Tous vos matchs de quartier, toute la saison.",
                color = PDZColor.Blanc,
                style = MaterialTheme.typography.headlineSmall,
            )
            Spacer(Modifier.height(PDZSpacing.space3))
            Text(
                text = "Les scores, les buteurs et le vainqueur de l'année, enfin gardés quelque part.",
                color = PDZColor.Blanc.copy(alpha = 0.85f),
                style = MaterialTheme.typography.bodyLarge,
            )
        }

        Column(modifier = Modifier.fillMaxWidth()) {
            PDZButton(
                text = "Créer mon compte",
                onClick = onCreateAccount,
                type = PDZButtonType.Primary,
            )
            Spacer(Modifier.height(PDZSpacing.space3))
            PDZButton(
                text = "J'ai déjà un compte",
                onClick = onLogin,
                type = PDZButtonType.Secondary,
            )
        }
    }
}
