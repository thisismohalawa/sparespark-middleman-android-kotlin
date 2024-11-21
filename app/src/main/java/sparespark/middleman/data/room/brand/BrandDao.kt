package sparespark.middleman.data.room.brand

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface BrandDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(roomBrand: RoomBrand): Long

    @Query("SELECT * FROM brand_table")
    suspend fun getList(): List<RoomBrand>

    @Query("SELECT * FROM brand_table WHERE creation_date = :itemId")
    suspend fun getBrandById(itemId: String): RoomBrand

    @Query("DELETE FROM brand_table")
    suspend fun clearList()
}
