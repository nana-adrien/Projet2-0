package empire.digiprem.projet2_0.server.amical.api

import empire.digiprem.projet2_0.contracts.amical.CreerMatchAmicalRequest
import empire.digiprem.projet2_0.server.amical.service.AmicalService
import empire.digiprem.projet2_0.server.common.dto.toApiResponse
import empire.digiprem.projet2_0.server.common.util.currentUserId
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

@RestController
@RequestMapping("/api/v1/clubs/{clubId}/matchs-amicaux", name = "MatchsAmicaux")
@Tag(name = "Matchs amicaux", description = "Organisation des rencontres amicales hors saison d'un 2-0")
class AmicalController(
    private val amicalService: AmicalService,
) {

    @Operation(
        summary = "Créer un match amical",
        description = "Organise un amical contre une équipe invitée (sans compte) avec sa date, son heure et la durée des mi-temps. Le match ne compte pas dans la saison du 2-0 (F-AMI-01, F-AMI-03, RG-14, RG-15)."
    )
    @PostMapping
    fun creerMatchAmical(
        @Parameter(description = "Identifiant du club organisateur") @PathVariable clubId: UUID,
        @RequestBody body: CreerMatchAmicalRequest,
    ) = amicalService.creerMatchAmical(clubId, UUID.fromString(currentUserId()), body).toApiResponse()
}
