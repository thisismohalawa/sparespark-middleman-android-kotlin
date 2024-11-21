package sparespark.middleman.data.room.product

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import sparespark.middleman.data.model.product.RelatedColor
import sparespark.middleman.data.model.product.RelatedUrl

@Entity(
    tableName = "product_table",
    indices = [Index("creation_date")]
)
data class RoomProduct(
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "creation_date")
    val creationDate: String,

    @ColumnInfo(name = "title")
    val title: String,

    @ColumnInfo(name = "des")
    val des: String,

    @ColumnInfo(name = "imgUrl")
    val imgUrl: String,

    @ColumnInfo(name = "brandId")
    val brandId: String,

    @ColumnInfo(name = "price")
    val price: Double,

    @ColumnInfo(name = "rating")
    val rating: Int,

    @ColumnInfo(name = "activated")
    val activated: Boolean,

    @ColumnInfo(name = "colors")
    val colors: ArrayList<RelatedColor>?,

    @ColumnInfo(name = "urls")
    val related: ArrayList<RelatedUrl>?
)
