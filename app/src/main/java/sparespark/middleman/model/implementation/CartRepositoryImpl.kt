package sparespark.middleman.model.implementation

import sparespark.middleman.common.Result
import sparespark.middleman.common.toCartProduct
import sparespark.middleman.common.toProductList
import sparespark.middleman.model.Product
import sparespark.middleman.model.repository.CartRepository
import sparespark.middleman.model.room.CartDao
import sparespark.middleman.model.room.CartProduct

class CartRepositoryImpl(
    private val local: CartDao
) : CartRepository {

    override suspend fun isCarted(productId: String): Result<Exception, Boolean> = Result.build {
        local.isCarted(productId)
    }

    override suspend fun addToCartList(product: Product): Result<Exception, Unit> = Result.build {
        local.upsert(product.toCartProduct)
    }

    override suspend fun removeFromCartList(productId: String): Result<Exception, Unit> =
        Result.build {
            local.clear(productId)
        }

    override suspend fun getCartList(): Result<Exception, List<Product>> = Result.build {
        local.getCartList().toProductList()
    }

    override suspend fun getTotalProductsPrice(): Result<Exception, Double> = Result.build {
        local.getTotalPrice()
    }
}