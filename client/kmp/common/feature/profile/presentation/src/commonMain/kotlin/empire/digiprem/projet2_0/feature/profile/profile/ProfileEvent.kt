package empire.digiprem.projet2_0.feature.profile.profile

sealed interface ProfileEvent {
    data object OnProfileUpdated : ProfileEvent
    /** Compte supprimé (RG-20) -> la session locale est déjà purgée, l'appelant doit déconnecter. */
    data object OnAccountDeleted : ProfileEvent
}
