package sparespark.middleman.model.room

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

    @Query("select name from brand_table where creation_date =:brandId")
    suspend fun getBrandName(brandId: String): String?

    @Query("DELETE FROM brand_table")
    suspend fun clearList()
}
