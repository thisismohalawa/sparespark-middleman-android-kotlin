package sparespark.middleman.data.repository

import sparespark.middleman.core.wrapper.Result
import sparespark.middleman.data.model.product.Product

interface ProductRepository {
    suspend fun getProductList(localOnly: Unit? = null): Result<Exception, List<Product>>
    suspend fun getProductById(productId: String): Result<Exception, Product>
}
