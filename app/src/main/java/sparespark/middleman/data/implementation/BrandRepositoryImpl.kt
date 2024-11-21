package sparespark.middleman.data.implementation

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import sparespark.middleman.core.DATABASE_URL
import sparespark.middleman.core.REF_BRAND_LIST
import sparespark.middleman.core.REF_DATABASE
import sparespark.middleman.core.mapping.toBrand
import sparespark.middleman.core.mapping.toBrandList
import sparespark.middleman.core.mapping.toRoomBrand
import sparespark.middleman.core.wrapper.Result
import sparespark.middleman.data.connectivity.ConnectivityInterceptor
import sparespark.middleman.data.implementation.base.BaseLocalUserPreferenceRepository
import sparespark.middleman.data.model.brand.Brand
import sparespark.middleman.data.model.brand.RemoteBrand
import sparespark.middleman.data.preferences.base.BaseListPreference
import sparespark.middleman.data.repository.BrandRepository
import sparespark.middleman.data.room.brand.BrandDao
import sparespark.middleman.data.room.user.UserDao
import sparespark.middleman.core.awaitTaskResult
import sparespark.middleman.core.launchASuspendTaskScope
import sparespark.middleman.core.mapping.toProduct
import sparespark.middleman.data.model.product.Product
import sparespark.middleman.data.model.product.RemoteProduct

class BrandRepositoryImpl(
    private val local: BrandDao,
    private val remote: DatabaseReference = FirebaseDatabase.getInstance(DATABASE_URL)
        .getReference(REF_DATABASE).child(REF_BRAND_LIST),
    private val pref: BaseListPreference,
    localUser: UserDao,
    connectInterceptor: ConnectivityInterceptor
) : BaseLocalUserPreferenceRepository(
    userDao = localUser,
    connectInterceptor = connectInterceptor
), BrandRepository {

    override suspend fun getBrands(localOnly: Unit?): Result<Exception, List<Brand>> =
        if (localOnly == Unit) getLocalList()
        else if (pref.isListUpdateNeeded()) getRemoteList()
        else getLocalList()

    override suspend fun getBrandById(brandId: String): Result<Exception, Brand> =
        if (pref.isListUpdateNeeded()) getRemoteItemById(brandId)
        else getLocalItemById(brandId)

    private suspend fun getLocalList(): Result<Exception, List<Brand>> = Result.build {
        local.getList().toBrandList()
    }


    private suspend fun getLocalItemById(itemId: String): Result<Exception, Brand> =
        Result.build {
            local.getBrandById(itemId).toBrand
        }

    private suspend fun List<Brand>.updateLocalEntries(): Result<Exception, Unit> =
        Result.build {
            launchASuspendTaskScope {
                if (this.isEmpty() || pref.isZeroInputCacheTime()) return@launchASuspendTaskScope
                local.clearList()
                this.forEach { local.upsert(it.toRoomBrand) }
                pref.updateCacheTimeToNow()
            }
        }

    /*====================
    * ================
    * REMOTE==============
    * ==================*/
    private suspend fun getRemoteItemById(itemId: String): Result<Exception, Brand> =
        Result.build {
            checkNetConnection()
            val task = awaitTaskResult(remote.child(itemId).get())
            task.getValue(RemoteBrand::class.java)?.toBrand ?: throw Exception()
        }

    private suspend fun getRemoteList(): Result<Exception, List<Brand>> =
        Result.build {
            checkNetConnection()
            val task = awaitTaskResult(remote.get())
            when (val result = resultToItemList(task)) {
                is Result.Error -> throw Exception()

                is Result.Value -> result.value.let {
                    it.updateLocalEntries()
                    return@build it
                }
            }
        }

    private suspend fun resultToItemList(result: DataSnapshot?): Result<Exception, List<Brand>> =
        Result.build {
            launchASuspendTaskScope {
                val itemList = mutableListOf<Brand>()
                result?.children?.forEach { remote ->
                    remote.getValue(RemoteBrand::class.java)?.let {
                        itemList.add(it.toBrand)
                    }
                }
                return@launchASuspendTaskScope itemList
            }
        }
}