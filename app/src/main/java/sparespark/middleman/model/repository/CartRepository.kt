package sparespark.middleman.model.repository

import sparespark.middleman.common.Result
import sparespark.middleman.model.Product

interface CartRepository {
    suspend fun isCarted(productId: String): Result<Exception, Boolean>
    suspend fun addToCartList(product: Product): Result<Exception, Unit>
    suspend fun removeFromCartList(productId: String): Result<Exception, Unit>
    suspend fun getCartList(): Result<Exception, List<Product>>
    suspend fun getTotalProductsPrice(): Result<Exception, Double>
}
