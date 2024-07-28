package sparespark.middleman.model.implementation

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import sparespark.middleman.common.BRAND_ID_REF
import sparespark.middleman.common.DATABASE_REF_NAME
import sparespark.middleman.common.DATABASE_URL
import sparespark.middleman.common.NoConnectivityException
import sparespark.middleman.common.PRODUCT_REF
import sparespark.middleman.common.Result
import sparespark.middleman.common.awaitQueryTaskResult
import sparespark.middleman.common.awaitTaskResult
import sparespark.middleman.common.toProduct
import sparespark.middleman.common.toProductListFromRoomProduct
import sparespark.middleman.common.toRoomProduct
import sparespark.middleman.model.Product
import sparespark.middleman.model.RemoteProduct
import sparespark.middleman.model.connectivity.ConnectivityInterceptor
import sparespark.middleman.model.preference.ProductPreference
import sparespark.middleman.model.repository.ProductRepository
import sparespark.middleman.model.room.ProductDao

class ProductRepositoryImpl(
    private val productPref: ProductPreference,
    private val local: ProductDao,
    private val remote: DatabaseReference = FirebaseDatabase.getInstance(DATABASE_URL)
        .getReference(DATABASE_REF_NAME).child(PRODUCT_REF),
    connectInterceptor: ConnectivityInterceptor
) : BaseRepository(connectInterceptor), ProductRepository {

    override suspend fun getProducts(): Result<Exception, List<Product>> =
        if (productPref.isListUpdateNeeded()) getRemoteItems()
        else getLocalItems()

    override suspend fun getLocalProducts(): Result<Exception, List<Product>> =
        getLocalItems()

    override suspend fun getProductsByBrandId(brandId: String): Result<Exception, List<Product>> =
        if (productPref.isListUpdateNeeded()) getRemoteItems(brandId)
        else getLocalItems(brandId)

    override suspend fun getProductById(productId: String): Result<Exception, Product> =
        Result.build {
            if (!hasInternetConnection()) throw NoConnectivityException()
            val task = awaitTaskResult(
                remote.child(productId).get()
            )
            task.getValue(RemoteProduct::class.java)?.toProduct ?: throw Exception()
        }


    private suspend fun getRemoteItems(): Result<Exception, List<Product>> =
        Result.build {
            if (!hasInternetConnection()) throw NoConnectivityException()
            val task = awaitTaskResult(remote.get())
            when (val result = resultToProductList(task)) {
                is Result.Error -> throw Exception()


                is Result.Value -> result.value.let {
                    it.updateLocalItemEntries()
                    return@build it
                }
            }
        }

    private suspend fun getRemoteItems(brandId: String): Result<Exception, List<Product>> =
        Result.build {
            if (!hasInternetConnection()) throw NoConnectivityException()
            val task = remote.awaitQueryTaskResult(brandId = brandId, refQuery = BRAND_ID_REF)
            when (val result = resultToProductList(task)) {
                is Result.Error -> throw Exception()


                is Result.Value -> result.value.let {
                    it.updateLocalItemEntries()
                    return@build it
                }
            }
        }

    private suspend fun resultToProductList(result: DataSnapshot?): Result<Exception, List<Product>> =
        withContext(Dispatchers.IO) {
            Result.build {
                if (result == null) return@build emptyList()
                val itemList = mutableListOf<Product>()
                result.children.forEach { data ->
                    data.getValue(RemoteProduct::class.java)?.let {
                        itemList.add(it.toProduct)
                    }
                }
                return@build itemList
            }
        }

    private suspend fun List<Product>.updateLocalItemEntries(): Result<Exception, Unit> =
        withContext(Dispatchers.IO) {
            Result.build {
                this@updateLocalItemEntries.let { remoteData ->
                    if (remoteData.isEmpty()) return@build
                    local.clearList()
                    remoteData.forEach { local.upsert(it.toRoomProduct) }
                    productPref.updateListCacheTimeToNow()
                }
            }
        }

    private suspend fun getLocalItems(): Result<Exception, List<Product>> =
        Result.build {
            local.getList().toProductListFromRoomProduct()
        }

    private suspend fun getLocalItems(brandId: String): Result<Exception, List<Product>> =
        Result.build {
            local.getBrandProductList(brandId).toProductListFromRoomProduct()
        }

}