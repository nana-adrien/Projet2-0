package empire.digiprem.projet2_0.feature.amical.creer

sealed interface CreerAmicalAction {
    data class OnInit(val clubId: String) : CreerAmicalAction
    data class OnNomEquipeChange(val value: String) : CreerAmicalAction
    data class OnDateChange(val value: String) : CreerAmicalAction
    data class OnHeureChange(val value: String) : CreerAmicalAction
    data class OnDureeMiTempsChange(val value: DureeMiTemps) : CreerAmicalAction
    data object OnCreerEtConvoquerClick : CreerAmicalAction
}
