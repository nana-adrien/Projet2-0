package empire.digiprem.projet2_0.server.statistiques.service

import empire.digiprem.projet2_0.server.calendrier.infra.entity.CompositionEntity
import empire.digiprem.projet2_0.server.calendrier.infra.entity.EvenementEntity
import java.util.UUID

internal const val TYPE_BUT = "BUT"
internal const val TYPE_BUT_CSC = "BUT_CONTRE_SON_CAMP"
internal const val TYPE_PENALTY_MARQUE = "PENALTY_MARQUE"
internal const val STATUT_OFFICIEL = "OFFICIEL"
internal const val STATUT_CLOTUREE = "CLOTUREE"
internal const val TYPE_MATCH_DEUX_ZERO = "DEUX_ZERO"

/** Agrégations pures (buts/passes/présences) à partir des événements et compositions d'un
 * ensemble de matchs — réutilisées par [StatistiquesService] (classements CU22) et
 * [BilanService] (meilleur buteur/passeur/assidu du bilan de saison CU25), pour ne pas dupliquer
 * la même règle de calcul à deux endroits. Seuls BUT et PENALTY_MARQUE comptent comme buts
 * personnels (BUT_CONTRE_SON_CAMP en est explicitement exclu, cf. RG classements) ; seul le
 * type BUT porte une passe décisive ([EvenementEntity.joueurSecondaire]) dans le modèle actuel.
 *
 * Objet utilitaire pur (pas un @Service Spring) : les relations LAZY [EvenementEntity.joueur]/
 * [EvenementEntity.joueurSecondaire]/[CompositionEntity.utilisateur] ne peuvent pas être protégées
 * par une annotation @Transactional ici — chaque appelant (StatistiquesService, BilanService,
 * ProfilProgressionService) doit garantir que les listes passées en paramètre ont été chargées
 * dans une transaction encore active au moment de l'appel (vérifié : tous les appelants sont
 * @Transactional). */
internal object StatistiquesAggregator {

    fun butsParJoueur(evenements: List<EvenementEntity>): Map<UUID, Int> =
        evenements
            .asSequence()
            .filter { !it.annule && (it.type == TYPE_BUT || it.type == TYPE_PENALTY_MARQUE) }
            .mapNotNull { it.joueur?.id }
            .groupingBy { it }
            .eachCount()

    fun passesParJoueur(evenements: List<EvenementEntity>): Map<UUID, Int> =
        evenements
            .asSequence()
            .filter { !it.annule && it.type == TYPE_BUT }
            .mapNotNull { it.joueurSecondaire?.id }
            .groupingBy { it }
            .eachCount()

    fun presencesParJoueur(compositions: List<CompositionEntity>): Map<UUID, Int> =
        compositions
            .asSequence()
            .filter { it.present }
            .groupingBy { it.utilisateur.id!! }
            .eachCount()

    /** Fusionne [buts] et [passes] en un total combiné par joueur (classement "buteur+passeur"). */
    fun combine(buts: Map<UUID, Int>, passes: Map<UUID, Int>): Map<UUID, Int> =
        (buts.keys + passes.keys).associateWith { (buts[it] ?: 0) + (passes[it] ?: 0) }
}
