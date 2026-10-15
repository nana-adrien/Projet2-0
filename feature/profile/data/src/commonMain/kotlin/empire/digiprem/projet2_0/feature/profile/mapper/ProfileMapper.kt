package empire.digiprem.projet2_0.feature.profile.mapper

import empire.digiprem.projet2_0.contracts.profile.ProfileDto
import empire.digiprem.projet2_0.contracts.profile.UpdateProfileRequest
import empire.digiprem.projet2_0.core.domain.model.PiedFort
import empire.digiprem.projet2_0.core.domain.model.Poste
import empire.digiprem.projet2_0.feature.profile.model.Profile

fun ProfileDto.toDomain(): Profile = Profile(
    id            = userId,
    pseudo        = pseudo,
    photo         = photo,
    nom           = nom,
    prenom        = prenom,
    dateNaissance = dateNaissance,
    postePrefere  = postePrefere?.let { raw -> Poste.entries.firstOrNull { it.name == raw } },
    piedFort      = piedFort?.let { raw -> PiedFort.entries.firstOrNull { it.name == raw } },
    cacherNomReel = cacherNomReel,
    cacherAge     = cacherAge,
)

fun Profile.toUpdateRequest(): UpdateProfileRequest = UpdateProfileRequest(
    pseudo         = pseudo,
    photo          = photo,
    nom            = nom,
    prenom         = prenom,
    dateNaissance  = dateNaissance,
    postePrefere   = postePrefere?.name,
    piedFort       = piedFort?.name,
    cacherNomReel  = cacherNomReel,
    cacherAge      = cacherAge,
)
