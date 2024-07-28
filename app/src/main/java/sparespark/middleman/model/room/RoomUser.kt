package sparespark.middleman.model.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

const val CURRENT_USER_ID = 0

@Entity(tableName = "user_table")
data class RoomUser(
    @ColumnInfo(name = "uid")
    val uid: String,

    @ColumnInfo(name = "name")
    val name: String,

    @ColumnInfo(name = "email")
    val email: String,

    @ColumnInfo(name = "activated")
    val activated: Boolean,
) {
    @PrimaryKey(autoGenerate = false)
    var id: Int = CURRENT_USER_ID
}
