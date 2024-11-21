package sparespark.middleman.data.repository

import sparespark.middleman.core.wrapper.Result
import sparespark.middleman.data.model.product.Product

interface CartRepository {
    suspend fun isCarted(productId: String): Result<Exception, Boolean>
    suspend fun addToCartList(product: Product): Result<Exception, Unit>
    suspend fun removeFromCartList(productId: String): Result<Exception, Unit>
    suspend fun getCartList(): Result<Exception, List<Product>>
    suspend fun getTotalProductsPrice(): Result<Exception, Double>
}
