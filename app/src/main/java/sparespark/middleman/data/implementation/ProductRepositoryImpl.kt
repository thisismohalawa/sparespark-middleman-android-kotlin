package sparespark.middleman.data.implementation

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import sparespark.middleman.core.DATABASE_URL
import sparespark.middleman.core.REF_DATABASE
import sparespark.middleman.core.REF_PRODUCT_LIST
import sparespark.middleman.core.awaitTaskResult
import sparespark.middleman.core.launchASuspendTaskScope
import sparespark.middleman.core.mapping.toProduct
import sparespark.middleman.core.mapping.toProductList
import sparespark.middleman.core.mapping.toRoomProduct
import sparespark.middleman.core.wrapper.Result
import sparespark.middleman.data.connectivity.ConnectivityInterceptor
import sparespark.middleman.data.implementation.base.BaseLocalUserPreferenceRepository
import sparespark.middleman.data.model.product.Product
import sparespark.middleman.data.model.product.RemoteProduct
import sparespark.middleman.data.preferences.base.BaseListPreference
import sparespark.middleman.data.repository.ProductRepository
import sparespark.middleman.data.room.product.ProductDao
import sparespark.middleman.data.room.user.UserDao

class ProductRepositoryImpl(
    private val local: ProductDao,
    private val remote: DatabaseReference = FirebaseDatabase.getInstance(DATABASE_URL)
        .getReference(REF_DATABASE).child(REF_PRODUCT_LIST),
    private val pref: BaseListPreference,
    localUser: UserDao,
    connectInterceptor: ConnectivityInterceptor
) : BaseLocalUserPreferenceRepository(
    userDao = localUser,
    connectInterceptor = connectInterceptor
), ProductRepository {


    override suspend fun getProductList(localOnly: Unit?): Result<Exception, List<Product>> =
        if (localOnly == Unit) getLocalList()
        else if (pref.isListUpdateNeeded()) getRemoteList()
        else getLocalList()

    override suspend fun getProductById(productId: String): Result<Exception, Product> =
        if (pref.isListUpdateNeeded()) getRemoteItemById(productId)
        else getLocalItemById(productId)

    private suspend fun getLocalList(): Result<Exception, List<Product>> = Result.build {
        local.getList().toProductList()
    }

    private suspend fun getLocalItemById(itemId: String): Result<Exception, Product> =
        Result.build {
            local.getProductById(itemId).toProduct
        }

    private suspend fun List<Product>.updateLocalEntries(): Result<Exception, Unit> =
        Result.build {
            launchASuspendTaskScope {
                if (this.isEmpty() || pref.isZeroInputCacheTime()) return@launchASuspendTaskScope
                local.clearList()
                this.forEach { local.upsert(it.toRoomProduct) }
                pref.updateCacheTimeToNow()
            }
        }

    /*====================
    * ================
    * REMOTE==============
    * ==================*/
    private suspend fun getRemoteItemById(itemId: String): Result<Exception, Product> =
        Result.build {
            checkNetConnection()
            val task = awaitTaskResult(remote.child(itemId).get())
            task.getValue(RemoteProduct::class.java)?.toProduct ?: throw Exception()
        }

    private suspend fun getRemoteList(): Result<Exception, List<Product>> =
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

    private suspend fun resultToItemList(result: DataSnapshot?): Result<Exception, List<Product>> =
        Result.build {
            launchASuspendTaskScope {
                val itemList = mutableListOf<Product>()
                result?.children?.forEach { remote ->
                    remote.getValue(RemoteProduct::class.java)?.let {
                        itemList.add(it.toProduct)
                    }
                }
                return@launchASuspendTaskScope itemList
            }
        }

}