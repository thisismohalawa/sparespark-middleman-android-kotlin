package sparespark.middleman.model.repository

import sparespark.middleman.common.Result
import sparespark.middleman.model.Product

interface ProductRepository {
    suspend fun getProducts(): Result<Exception, List<Product>>
    suspend fun getLocalProducts(): Result<Exception, List<Product>>
    suspend fun getProductsByBrandId(brandId: String): Result<Exception, List<Product>>
    suspend fun getProductById(productId: String): Result<Exception, Product>
}