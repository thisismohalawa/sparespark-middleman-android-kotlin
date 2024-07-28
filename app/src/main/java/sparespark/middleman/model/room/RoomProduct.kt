package sparespark.middleman.model.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import sparespark.middleman.model.IColor
import sparespark.middleman.model.IRelated

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

    @ColumnInfo(name = "activated")
    val activated: Boolean,

    @ColumnInfo(name = "colors")
    val colors: ArrayList<IColor>?,

    @ColumnInfo(name = "related")
    val related: ArrayList<IRelated>?,
)
