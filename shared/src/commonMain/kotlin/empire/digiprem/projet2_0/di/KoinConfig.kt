package empire.digiprem.projet2_0.di

import empire.digiprem.projet2_0.core.config.coreModuleData
import empire.digiprem.projet2_0.feature.auth.config.koinAuthModule
import empire.digiprem.projet2_0.feature.calendrier.config.koinCalendrierModule
import empire.digiprem.projet2_0.feature.clubs.config.koinClubsModule
import empire.digiprem.projet2_0.feature.dashboard.config.koinDashboardModule
import empire.digiprem.projet2_0.feature.deuxzero.config.koinDeuxZeroModule
import empire.digiprem.projet2_0.feature.notifications.config.koinNotificationsModule
import empire.digiprem.projet2_0.feature.profile.config.koinProfileModule
import empire.digiprem.projet2_0.feature.settings.config.koinSettingsModule
import org.koin.core.context.startKoin
import org.koin.dsl.KoinAppDeclaration

fun initKoin(config: KoinAppDeclaration? = null) {
    startKoin {
        config?.invoke(this)
        modules(
            coreModuleData,
            koinAuthModule,
            koinProfileModule,
            koinNotificationsModule,
            koinSettingsModule,
            koinClubsModule,
            koinDeuxZeroModule,
            koinCalendrierModule,
            koinDashboardModule,
        )
    }
}
