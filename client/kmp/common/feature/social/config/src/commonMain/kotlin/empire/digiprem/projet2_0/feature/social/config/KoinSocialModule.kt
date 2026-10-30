package empire.digiprem.projet2_0.feature.social.config

import empire.digiprem.projet2_0.feature.social.datasource.ISocialDataSource
import empire.digiprem.projet2_0.feature.social.datasource.SocialKtorDataSource
import empire.digiprem.projet2_0.feature.social.repository.ISocialRepository
import empire.digiprem.projet2_0.feature.social.repository.SocialRepository
import empire.digiprem.projet2_0.feature.social.signaler.SignalerViewModel
import empire.digiprem.projet2_0.feature.social.usecase.SignalerUseCase
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.bind
import org.koin.dsl.module

val koinSocialModule = module {
    singleOf(::SocialKtorDataSource) bind ISocialDataSource::class
    singleOf(::SocialRepository)     bind ISocialRepository::class
    singleOf(::SignalerUseCase)
    viewModelOf(::SignalerViewModel)
}
