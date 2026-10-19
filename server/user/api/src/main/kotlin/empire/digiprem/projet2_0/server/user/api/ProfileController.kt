package empire.digiprem.projet2_0.server.user.api

import empire.digiprem.projet2_0.contracts.profile.UpdateProfileRequest
import empire.digiprem.projet2_0.contracts.base.ApiResponse
import empire.digiprem.projet2_0.server.common.dto.toApiResponse
import empire.digiprem.projet2_0.server.common.util.currentUserId
import empire.digiprem.projet2_0.server.user.service.ProfileService
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

/** CU4 — Modifier son profil (et supprimer son compte). */
@RestController
@RequestMapping("/api/v1/profile", name = "Profile")
class ProfileController(
    private val profileService: ProfileService,
) {

    @GetMapping
    fun getProfile() = profileService.getProfile(currentUserId()).toApiResponse()

    @PutMapping
    fun updateProfile(@RequestBody body: UpdateProfileRequest) =
        profileService.updateProfile(currentUserId(), body).toApiResponse()

    @DeleteMapping
    fun deleteAccount(): ApiResponse {
        profileService.deleteAccount(currentUserId())
        return ApiResponse.ok()
    }
}
