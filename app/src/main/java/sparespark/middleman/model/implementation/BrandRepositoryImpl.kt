package sparespark.middleman.model.implementation

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import sparespark.middleman.common.BRAND_REF
import sparespark.middleman.common.DATABASE_REF_NAME
import sparespark.middleman.common.DATABASE_URL
import sparespark.middleman.common.NoConnectivityException
import sparespark.middleman.common.Result
import sparespark.middleman.common.awaitTaskResult
import sparespark.middleman.common.toBrand
import sparespark.middleman.common.toBrandListFromRoomBrand
import sparespark.middleman.common.toRoomBrand
import sparespark.middleman.model.Brand
import sparespark.middleman.model.RemoteBrand
import sparespark.middleman.model.connectivity.ConnectivityInterceptor
import sparespark.middleman.model.preference.BrandPreferenceImpl
import sparespark.middleman.model.repository.BrandRepository
import sparespark.middleman.model.room.BrandDao

class BrandRepositoryImpl(
    private val brandPref: BrandPreferenceImpl,
    private val remote: DatabaseReference = FirebaseDatabase.getInstance(DATABASE_URL)
        .getReference(DATABASE_REF_NAME).child(BRAND_REF),
    private val local: BrandDao,
    connectInterceptor: ConnectivityInterceptor
) : BaseRepository(connectInterceptor), BrandRepository {

    override suspend fun getBrands(): Result<Exception, List<Brand>> =
        if (brandPref.isListUpdateNeeded()) getRemoteItems()
        else getLocalItems()

    override suspend fun getLocalBrands(): Result<Exception, List<Brand>> =
        getLocalItems()

    override suspend fun getBrandById(brandId: String): Result<Exception, Brand> = Result.build {
        if (!hasInternetConnection()) throw NoConnectivityException()
        val task = awaitTaskResult(
            remote.child(brandId).get()
        )
        task.getValue(RemoteBrand::class.java)?.toBrand ?: throw Exception()
    }

    override suspend fun getBrandNameById(brandId: String): Result<Exception, String?> =
        Result.build {
            local.getBrandName(brandId)
        }

    private suspend fun getRemoteItems(): Result<Exception, List<Brand>> = Result.build {
        if (!hasInternetConnection()) throw NoConnectivityException()
        val task = awaitTaskResult(remote.get())
        when (val result = resultToBrandList(task)) {
            is Result.Error -> throw Exception()
            is Result.Value -> result.value.let {
                it.updateLocalItemEntries()
                return@build it
            }
        }
    }

    private suspend fun resultToBrandList(result: DataSnapshot?): Result<Exception, List<Brand>> =
        withContext(Dispatchers.IO) {
            Result.build {
                if (result == null) return@build emptyList()
                val itemList = mutableListOf<Brand>()
                result.children.forEach { data ->
                    data.getValue(RemoteBrand::class.java)?.let {
                        if (it.activated == true) itemList.add(it.toBrand)
                    }
                }
                return@build itemList
            }
        }

    private suspend fun List<Brand>.updateLocalItemEntries(): Result<Exception, Unit> =
        withContext(Dispatchers.IO) {
            Result.build {
                this@updateLocalItemEntries.let { remoteData ->
                    if (remoteData.isEmpty()) return@build
                    local.clearList()
                    remoteData.forEach { local.upsert(it.toRoomBrand) }
                    brandPref.updateListCacheTimeToNow()
                }
            }
        }

    private suspend fun getLocalItems(): Result<Exception, List<Brand>> = Result.build {
        local.getList().toBrandListFromRoomBrand()
    }
}