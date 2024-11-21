package sparespark.middleman.data.room.user

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(user: RoomUser): Long

    @Query("SELECT EXISTS(SELECT * FROM user_table)")
    suspend fun isExist(): Boolean

    @Query("select * from user_table where id = $CURRENT_USER_ID")
    suspend fun getUser(): RoomUser?

    @Query("select uid from user_table where id = $CURRENT_USER_ID")
    suspend fun getUserId(): String?

    @Query("DELETE FROM user_table")
    suspend fun deleteUser()
}
