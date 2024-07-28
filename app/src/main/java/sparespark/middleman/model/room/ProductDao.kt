package sparespark.middleman.model.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface ProductDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(roomProduct: RoomProduct): Long

    @Query("SELECT * FROM product_table")
    suspend fun getList(): List<RoomProduct>

    @Query("SELECT * FROM product_table where brandId = :brandId")
    suspend fun getBrandProductList(brandId: String): List<RoomProduct>
    
    @Query("DELETE FROM product_table")
    suspend fun clearList()
}
