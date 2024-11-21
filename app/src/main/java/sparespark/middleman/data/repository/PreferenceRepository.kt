package sparespark.middleman.data.repository

import sparespark.middleman.core.wrapper.Result

interface PreferenceRepository {
    suspend fun clearPrefLastCacheTimes(): Result<Exception, Unit>
    suspend fun clearDatabase(): Result<Exception, Unit>
}