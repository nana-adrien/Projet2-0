package empire.digiprem.projet2_0.core.data.security

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import co.touchlab.kermit.Logger
import empire.digiprem.projet2_0.core.data.dto.AuthSessionSerializable
import empire.digiprem.projet2_0.core.data.mappers.toDomain
import empire.digiprem.projet2_0.core.data.mappers.toSerializable
import empire.digiprem.projet2_0.core.domain.model.AuthSession
import empire.digiprem.projet2_0.core.domain.service.AuthSessionStorage
import empire.digiprem.projet2_0.core.domain.service.CryptoManager
import kotlinx.serialization.json.Json

class AuthSessionStorageServiceImpl(
    cryptoManager: CryptoManager,
    dataStore: DataStore<Preferences>,
    json: Json,
    logger: Logger,
) : SecureStorageService<AuthSession, AuthSessionSerializable>(
    key            = "AuthSession",
    cryptoManager  = cryptoManager,
    dataStore      = dataStore,
    serializer     = AuthSessionSerializable.serializer(),
    toDomain       = { it.toDomain() },
    toSerializable = { it.toSerializable() },
    json           = json,
    logger         = logger,
), AuthSessionStorage
