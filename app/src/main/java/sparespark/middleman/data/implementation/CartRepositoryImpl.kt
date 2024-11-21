package sparespark.middleman.data.implementation

import sparespark.middleman.core.mapping.toCartProduct
import sparespark.middleman.core.mapping.toProductList
import sparespark.middleman.core.wrapper.Result
import sparespark.middleman.data.model.product.Product
import sparespark.middleman.data.repository.CartRepository
import sparespark.middleman.data.room.cart.CartDao

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