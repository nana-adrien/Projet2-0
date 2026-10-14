package empire.digiprem.projet2_0.feature.clubs.mapper

import empire.digiprem.projet2_0.feature.clubs.dto.ClubDto
import empire.digiprem.projet2_0.feature.clubs.dto.MembershipDto
import empire.digiprem.projet2_0.feature.clubs.model.Club
import empire.digiprem.projet2_0.feature.clubs.model.ClubStatut
import empire.digiprem.projet2_0.feature.clubs.model.Membership
import empire.digiprem.projet2_0.feature.clubs.model.MembershipRole
import empire.digiprem.projet2_0.feature.clubs.model.MembershipStatut

fun ClubDto.toDomain() = Club(
    id = id,
    nom = nom,
    logo = logo,
    couleurs = couleurs,
    quartier = quartier,
    description = description,
    codeAdhesion = codeAdhesion,
    statut = ClubStatut.valueOf(statut),
    acceptationAutomatique = acceptationAutomatique,
    delegationCapitaines = delegationCapitaines,
)

fun MembershipDto.toDomain() = Membership(
    id = id,
    clubId = clubId,
    clubNom = clubNom,
    clubLogo = clubLogo,
    utilisateurId = utilisateurId,
    pseudo = pseudo,
    role = MembershipRole.valueOf(role),
    statut = MembershipStatut.valueOf(statut),
    dateDemande = dateDemande,
    dateReponse = dateReponse,
)
