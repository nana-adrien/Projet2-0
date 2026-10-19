package empire.digiprem.projet2_0.server.user.api

import empire.digiprem.projet2_0.contracts.profile.UpdateProfileRequest
import empire.digiprem.projet2_0.contracts.base.ApiResponse
import empire.digiprem.projet2_0.server.common.dto.toApiResponse
import empire.digiprem.projet2_0.server.common.util.currentUserId
import empire.digiprem.projet2_0.server.user.service.ProfileService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

/** CU4 — Modifier son profil (et supprimer son compte). */
@RestController
@RequestMapping("/api/v1/profile", name = "Profile")
@Tag(name = "Profil", description = "Consultation, modification et suppression du compte du joueur connecté")
class ProfileController(
    private val profileService: ProfileService,
) {

    @Operation(
        summary = "Consulter mon profil",
        description = "Profil du joueur connecté : pseudo, photo, poste préféré, pied fort et informations personnelles (F-CPT-05)."
    )
    @GetMapping
    fun getProfile() = profileService.getProfile(currentUserId()).toApiResponse()

    @Operation(
        summary = "Modifier mon profil",
        description = "Met à jour le pseudo, la photo, le poste, le pied fort et les informations personnelles (F-CPT-05)."
    )
    @PutMapping
    fun updateProfile(@RequestBody body: UpdateProfileRequest) =
        profileService.updateProfile(currentUserId(), body).toApiResponse()

    @Operation(
        summary = "Supprimer mon compte",
        description = "Supprime le compte à la demande du joueur ; l'historique sportif est anonymisé et conservé (F-CPT-07, RG-20)."
    )
    @DeleteMapping
    fun deleteAccount(): ApiResponse {
        profileService.deleteAccount(currentUserId())
        return ApiResponse.ok()
    }
}
