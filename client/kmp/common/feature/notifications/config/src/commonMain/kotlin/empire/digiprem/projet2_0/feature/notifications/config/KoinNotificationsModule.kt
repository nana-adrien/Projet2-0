package empire.digiprem.projet2_0.feature.notifications.config

import empire.digiprem.projet2_0.feature.notifications.datasource.INotificationDataSource
import empire.digiprem.projet2_0.feature.notifications.datasource.NotificationKtorDataSource
import empire.digiprem.projet2_0.feature.notifications.notifications.NotificationsViewModel
import empire.digiprem.projet2_0.feature.notifications.preferences.PreferenceViewModel
import empire.digiprem.projet2_0.feature.notifications.repository.INotificationRepository
import empire.digiprem.projet2_0.feature.notifications.repository.NotificationRepository
import empire.digiprem.projet2_0.feature.notifications.usecase.DeleteNotificationUseCase
import empire.digiprem.projet2_0.feature.notifications.usecase.GetNotificationsUseCase
import empire.digiprem.projet2_0.feature.notifications.usecase.GetPreferenceNotificationUseCase
import empire.digiprem.projet2_0.feature.notifications.usecase.MarkAsReadUseCase
import empire.digiprem.projet2_0.feature.notifications.usecase.SetPreferenceNotificationUseCase
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.bind
import org.koin.dsl.module

val koinNotificationsModule = module {
    singleOf(::NotificationKtorDataSource) bind INotificationDataSource::class
    singleOf(::NotificationRepository)     bind INotificationRepository::class
    singleOf(::GetNotificationsUseCase)
    singleOf(::MarkAsReadUseCase)
    singleOf(::DeleteNotificationUseCase)
    singleOf(::GetPreferenceNotificationUseCase)
    singleOf(::SetPreferenceNotificationUseCase)
    viewModelOf(::NotificationsViewModel)
    viewModelOf(::PreferenceViewModel)
}
