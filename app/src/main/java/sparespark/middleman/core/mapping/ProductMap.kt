package sparespark.middleman.core.mapping

import sparespark.middleman.core.BUY_MSG
import sparespark.middleman.data.model.product.Product
import sparespark.middleman.data.model.product.RemoteProduct
import sparespark.middleman.data.room.product.RoomProduct

internal val RoomProduct.toProduct: Product
    get() = Product(
        creationDate = this.creationDate,
        title = this.title,
        des = this.des,
        imgUrl = this.imgUrl,
        brandId = this.brandId,
        price = this.price,
        rating = this.rating,
        activated = this.activated,
        colors = this.colors ?: ArrayList(),
        related = this.related ?: ArrayList(),
    )
internal val RemoteProduct.toProduct: Product
    get() = Product(
        creationDate = this.creationDate ?: "",
        title = this.title ?: "",
        des = this.des ?: "",
        imgUrl = this.imgUrl ?: "",
        brandId = this.brandId ?: "",
        price = this.price ?: 0.0,
        rating = this.rating ?: 0,
        activated = this.activated ?: false,
        colors = this.colors ?: ArrayList(),
        related = this.related ?: ArrayList(),
    )
internal val Product.toRoomProduct: RoomProduct
    get() = RoomProduct(
        creationDate = this.creationDate,
        title = this.title,
        des = this.des,
        imgUrl = this.imgUrl,
        brandId = this.brandId,
        price = this.price,
        rating = this.rating,
        activated = this.activated,
        colors = this.colors,
        related = this.related,
    )
internal val Product.toRemoteProduct: RemoteProduct
    get() = RemoteProduct(
        creationDate = this.creationDate,
        title = this.title,
        brandId = this.brandId,
        des = this.des,
        imgUrl = this.imgUrl,
        price = this.price,
        activated = this.activated,
    )

internal fun List<RoomProduct>.toProductList(): List<Product> = this.flatMap {
    listOf(it.toProduct)
}

internal fun List<Product>.toOrderTitleFromProductList(): String {
    var order = BUY_MSG
    this.forEach {
        order += "\n\nId:${it.creationDate}\n" + "${it.title} (${it.price})\n"
    }
    return order
}