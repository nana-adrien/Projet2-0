package empire.digiprem.projet2_0.core.config

import empire.digiprem.projet2_0.core.data.security.AndroidCryptoManager
import empire.digiprem.projet2_0.core.data.utils.createDataStore
import empire.digiprem.projet2_0.core.domain.service.CryptoManager
import io.ktor.client.engine.okhttp.OkHttp
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

actual val platformCoreModuleData = module {
    single<CryptoManager> { AndroidCryptoManager() }
    single { createDataStore(androidContext()) }
    single { OkHttp.create() }
}
