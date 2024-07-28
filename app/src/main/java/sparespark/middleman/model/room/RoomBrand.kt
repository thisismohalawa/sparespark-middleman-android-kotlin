package sparespark.middleman.model.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "brand_table", indices = [Index("creation_date")])
data class RoomBrand(
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "creation_date")
    val creationDate: String,

    @ColumnInfo(name = "name")
    val name: String,

    @ColumnInfo(name = "title")
    val title: String,

    @ColumnInfo(name = "imgUrl")
    val imgUrl: String,

    @ColumnInfo(name = "linkUrl")
    val linkUrl: String,

    @ColumnInfo(name = "activated")
    val activated: Boolean,
)
