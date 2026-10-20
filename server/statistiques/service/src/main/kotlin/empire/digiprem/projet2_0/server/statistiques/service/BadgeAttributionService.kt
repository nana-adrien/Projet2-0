package empire.digiprem.projet2_0.server.statistiques.service

import empire.digiprem.projet2_0.contracts.statistiques.BadgeObtenuDto
import empire.digiprem.projet2_0.server.calendrier.infra.repository.EvenementRepository
import empire.digiprem.projet2_0.server.calendrier.infra.repository.MatchRepository
import empire.digiprem.projet2_0.server.common.exception.ForbiddenActionException
import empire.digiprem.projet2_0.server.common.exception.ResourceNotFoundException
import empire.digiprem.projet2_0.server.statistiques.infra.entity.BadgeObtenuEntity
import empire.digiprem.projet2_0.server.statistiques.infra.repository.BadgeObtenuRepository
import empire.digiprem.projet2_0.server.user.infra.repository.UserRepository
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service
import java.util.UUID

private const val STATUT_OFFICIEL_MATCH = "OFFICIEL"

private const val BADGE_PREMIER_BUT = "PREMIER_BUT"
private const val BADGE_DOUBLE = "DOUBLE"
private const val BADGE_TRIPLE = "TRIPLE"
private const val BADGE_QUADRUPLE = "QUADRUPLE"
private const val BADGE_PASSEUR_DE_GENIE = "PASSEUR_DE_GENIE"

/** F-REC-02 — seuil d'attribution de PASSEUR_DE_GENIE. Aucune règle quantitative n'est précisée
 * dans l'UML ni les maquettes pour ce badge (contrairement à DOUBLE/TRIPLE/QUADRUPLE qui sont
 * numériquement explicites) : 2 passes décisives dans un même match est une **interprétation**
 * raisonnable de Kako, pas une règle produit confirmée. À ajuster si le produit en précise une. */
private const val SEUIL_PASSEUR_DE_GENIE = 2

/**
 * CU24 (partie badges, F-REC-02) — attribution des badges de performance. Déclenché
 * manuellement (`POST .../badges/calculer`) en l'absence d'un hook sur l'officialisation du
 * match (CU19/CU20, pas encore livré) : à appeler une fois le match passé à OFFICIEL.
 *
 * **Badges implémentés** (calculables sans ambiguïté à partir des [EvenementEntity] réellement
 * produites) : PREMIER_BUT (à vie, une seule fois), DOUBLE/TRIPLE/QUADRUPLE (buts BUT ou
 * PENALTY_MARQUE, BUT_CONTRE_SON_CAMP exclu, dans un même match), PASSEUR_DE_GENIE (seuil
 * documenté ci-dessus).
 *
 * **Badges NON implémentés** (limitation produit, pas un oubli) : FIDELE, MUR, INVAINCU,
 * HOMME_FORT, TOUJOURS_LA, PERSONNALISE — aucune règle quantitative n'existe dans l'UML ni les
 * maquettes (juste les noms), et MUR/INVAINCU nécessiteraient des données de buts encaissés par
 * gardien / clean-sheet qui n'existent pas dans le modèle Evenement actuel.
 *
 * **Idempotence.** [BadgeObtenuEntity] ne référence pas de match (choix UML délibéré, cf. son
 * KDoc) : pour éviter qu'un second appel sur le même match ne duplique les badges "par match"
 * (DOUBLE/TRIPLE/QUADRUPLE/PASSEUR_DE_GENIE), [matchId] est inclus explicitement dans le texte
 * de [BadgeObtenuEntity.contexte], et on vérifie l'absence d'un badge au contexte strictement
 * identique avant insertion. PREMIER_BUT est naturellement idempotent (un seul par utilisateur
 * à vie, via [BadgeObtenuRepository.existsByUtilisateur_IdAndType]).
 */
@Service
class BadgeAttributionService(
    private val matchRepository: MatchRepository,
    private val evenementRepository: EvenementRepository,
    private val badgeObtenuRepository: BadgeObtenuRepository,
    private val userRepository: UserRepository,
) {

    @Transactional
    fun calculerBadges(matchId: UUID): List<BadgeObtenuDto> {
        val match = matchRepository.findById(matchId).orElseThrow { ResourceNotFoundException("Match introuvable") }
        if (match.statut != STATUT_OFFICIEL_MATCH) {
            throw ForbiddenActionException("Les badges ne sont calculés que pour un match OFFICIEL")
        }

        val evenements = evenementRepository.findAllByMatch_IdOrderByPeriodeAscMinuteAsc(matchId).filter { !it.annule }
        val nouveauxBadges = mutableListOf<BadgeObtenuEntity>()

        val butsParJoueur = StatistiquesAggregator.butsParJoueur(evenements)
        butsParJoueur.forEach { (joueurId, nombreDeButs) ->
            if (!badgeObtenuRepository.existsByUtilisateur_IdAndType(joueurId, BADGE_PREMIER_BUT)) {
                ajouterSiAbsent(nouveauxBadges, joueurId, BADGE_PREMIER_BUT, "Match du ${match.date} — 1er but de la carrière")
            }
            val typeMultiple = when {
                nombreDeButs >= 4 -> BADGE_QUADRUPLE
                nombreDeButs == 3 -> BADGE_TRIPLE
                nombreDeButs == 2 -> BADGE_DOUBLE
                else -> null
            }
            if (typeMultiple != null) {
                ajouterSiAbsent(
                    nouveauxBadges, joueurId, typeMultiple,
                    "Match $matchId du ${match.date} — $nombreDeButs buts",
                )
            }
        }

        val passesParJoueur = StatistiquesAggregator.passesParJoueur(evenements)
        passesParJoueur.filterValues { it >= SEUIL_PASSEUR_DE_GENIE }.forEach { (joueurId, nombreDePasses) ->
            ajouterSiAbsent(
                nouveauxBadges, joueurId, BADGE_PASSEUR_DE_GENIE,
                "Match $matchId du ${match.date} — $nombreDePasses passes décisives",
            )
        }

        return badgeObtenuRepository.saveAll(nouveauxBadges).map { it.toDto() }
    }

    @Transactional
    fun getBadgesUtilisateur(userId: UUID): List<BadgeObtenuDto> =
        badgeObtenuRepository.findAllByUtilisateur_Id(userId).map { it.toDto() }

    private fun ajouterSiAbsent(
        accumulateur: MutableList<BadgeObtenuEntity>,
        utilisateurId: UUID,
        type: String,
        contexte: String,
    ) {
        val dejaObtenu = badgeObtenuRepository.findAllByUtilisateur_Id(utilisateurId).any { it.type == type && it.contexte == contexte }
        if (!dejaObtenu) {
            accumulateur += BadgeObtenuEntity(utilisateur = userRepository.getReferenceById(utilisateurId), type = type, contexte = contexte)
        }
    }

    /** Accès à la relation LAZY BadgeObtenuEntity.utilisateur : appelants (calculerBadges,
     * getBadgesUtilisateur) déjà @Transactional. */
    private fun BadgeObtenuEntity.toDto() = BadgeObtenuDto(
        id = id.toString(),
        utilisateurId = utilisateur.id.toString(),
        type = type,
        contexte = contexte,
        date = date.toString(),
    )
}
