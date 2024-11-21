package sparespark.middleman.core.mapping

import sparespark.middleman.data.model.product.Product
import sparespark.middleman.data.room.cart.RoomCartProduct


internal val RoomCartProduct.toProduct: Product
    get() = Product(
        creationDate = this.creationDate,
        title = this.title,
        des = this.desc,
        imgUrl = this.imgUrl,
        brandId = this.brandId,
        price = this.price,
        rating = 0,
        activated = true,
        colors = ArrayList(),
        related = ArrayList(),
    )
internal val Product.toCartProduct: RoomCartProduct
    get() = RoomCartProduct(
        creationDate = this.creationDate,
        title = this.title,
        desc = this.des,
        imgUrl = this.imgUrl,
        price = this.price,
        brandId = this.brandId
    )

internal fun List<RoomCartProduct>.toProductList(): List<Product> = this.flatMap {
    listOf(it.toProduct)
}
