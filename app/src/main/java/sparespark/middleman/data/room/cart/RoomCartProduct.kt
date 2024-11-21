package sparespark.middleman.data.room.cart

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "cart_table",
    indices = [Index("creation_date")]
)
data class RoomCartProduct(
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "creation_date")
    val creationDate: String,

    @ColumnInfo(name = "title")
    val title: String,

    @ColumnInfo(name = "desc")
    val desc: String,

    @ColumnInfo(name = "brand_id")
    val brandId: String,

    @ColumnInfo(name = "imgUrl")
    val imgUrl: String,

    @ColumnInfo(name = "price")
    val price: Double,
)
