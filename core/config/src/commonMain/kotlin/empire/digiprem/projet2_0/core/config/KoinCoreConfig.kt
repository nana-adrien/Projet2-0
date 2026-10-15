package empire.digiprem.projet2_0.core.config

import co.touchlab.kermit.Logger
import empire.digiprem.projet2_0.core.data.networking.HttpClientFactory
import empire.digiprem.projet2_0.core.data.security.AuthSessionStorageServiceImpl
import empire.digiprem.projet2_0.core.domain.service.AuthSessionStorage
import io.ktor.client.HttpClient
import kotlinx.serialization.json.Json
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val coreModuleData = module {
    includes(platformCoreModuleData)

    single {
        Json {
            ignoreUnknownKeys  = true
            isLenient          = true
            coerceInputValues  = true
            encodeDefaults     = false
        }
    }

    single { Logger.withTag("Projet2-0") }

    singleOf(::AuthSessionStorageServiceImpl) bind AuthSessionStorage::class

    single<HttpClient> {
        HttpClientFactory(
            engine              = get(),
            authSessionStorage  = get(),
            logger              = get(),
        ).create()
    }
}
