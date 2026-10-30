package empire.digiprem.projet2_0.feature.profile.model

import empire.digiprem.projet2_0.core.domain.model.PiedFort
import empire.digiprem.projet2_0.core.domain.model.Poste

/** CU4 — Profil éditable (UML `Utilisateur`, champs exposés par ProfileController). */
data class Profile(
    val id             : String,
    val pseudo         : String,
    val photo          : String?  = null,
    val nom            : String?  = null,
    val prenom         : String?  = null,
    val dateNaissance  : String?  = null,
    val postePrefere   : Poste?   = null,
    val piedFort       : PiedFort? = null,
    val cacherNomReel  : Boolean  = false,
    val cacherAge      : Boolean  = false,
)
