package empire.digiprem.projet2_0.feature.deuxzero.parametres

sealed interface ParametresDeuxZeroEvent {
    /** Le rôle d'organisateur vient d'être transféré -> l'écran n'a plus lieu d'être pour l'utilisateur actuel. */
    data object OnOrganisateurTransfere : ParametresDeuxZeroEvent
}
