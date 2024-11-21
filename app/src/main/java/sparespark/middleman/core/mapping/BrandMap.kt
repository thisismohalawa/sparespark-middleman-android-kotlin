package sparespark.middleman.core.mapping

import sparespark.middleman.data.model.brand.Brand
import sparespark.middleman.data.model.brand.RemoteBrand
import sparespark.middleman.data.room.brand.RoomBrand

internal val RoomBrand.toBrand: Brand
    get() = Brand(
        creationDate = this.creationDate,
        name = this.name,
        title = this.title,
        imgUrl = this.imgUrl,
        linkUrl = this.linkUrl,
        activated = this.activated
    )
internal val RemoteBrand.toBrand: Brand
    get() = Brand(
        creationDate = this.creationDate ?: "",
        name = this.name ?: "",
        title = this.title ?: "",
        imgUrl = this.imgUrl ?: "",
        linkUrl = this.linkUrl ?: "",
        activated = this.activated ?: false
    )
internal val Brand.toRemoteBrand: RemoteBrand
    get() = RemoteBrand(
        creationDate = this.creationDate,
        name = this.name,
        title = this.title,
        imgUrl = this.imgUrl,
        linkUrl = this.linkUrl,
        activated = this.activated
    )
internal val Brand.toRoomBrand: RoomBrand
    get() = RoomBrand(
        creationDate = this.creationDate,
        name = this.name,
        title = this.title,
        imgUrl = this.imgUrl,
        linkUrl = this.linkUrl,
        activated = this.activated
    )

internal fun List<RoomBrand>.toBrandList(): List<Brand> = this.flatMap {
    listOf(it.toBrand)
}
