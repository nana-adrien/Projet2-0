package empire.digiprem.projet2_0.contracts.validation

import empire.digiprem.projet2_0.contracts.notifications.NiveauNotification
import empire.digiprem.projet2_0.contracts.notifications.SetPreferenceNotificationRequest
import io.konform.validation.Validation
import io.konform.validation.constraints.enum

val validateSetPreferenceNotificationRequest = Validation<SetPreferenceNotificationRequest> {
    SetPreferenceNotificationRequest::niveau {
        enum<NiveauNotification>()
    }
}
