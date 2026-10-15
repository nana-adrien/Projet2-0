package empire.digiprem.projet2_0.core.config

import empire.digiprem.projet2_0.core.data.security.IosCryptoManager
import empire.digiprem.projet2_0.core.data.utils.createDataStore
import empire.digiprem.projet2_0.core.domain.service.CryptoManager
import io.ktor.client.engine.darwin.Darwin
import org.koin.dsl.module

actual val platformCoreModuleData = module {
    single<CryptoManager> { IosCryptoManager() }
    single { createDataStore() }
    single { Darwin.create() }
}
