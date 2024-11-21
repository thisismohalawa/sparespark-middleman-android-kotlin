package sparespark.middleman.data.implementation

import sparespark.middleman.core.wrapper.Result
import sparespark.middleman.data.preferences.cache.CachePreference
import sparespark.middleman.data.repository.PreferenceRepository
import sparespark.middleman.data.room.MiddlemanDatabase
import sparespark.middleman.core.launchASuspendTaskScope

class PreferenceRepositoryImpl(
    private val localDB: MiddlemanDatabase,
    private val cachePreference: CachePreference
) : PreferenceRepository {
    override suspend fun clearPrefLastCacheTimes(): Result<Exception, Unit> = Result.build {
        cachePreference.clearLastCacheTimes()
    }

    override suspend fun clearDatabase(): Result<Exception, Unit> = Result.build {
        launchASuspendTaskScope {
            localDB.clearAllTables()
        }
    }
}