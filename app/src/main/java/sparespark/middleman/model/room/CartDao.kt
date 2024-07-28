package sparespark.middleman.model.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface CartDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(product: CartProduct): Long

    @Query("select exists(select * from cart_table WHERE creation_date = :productId)")
    suspend fun isCarted(productId: String): Boolean

    @Query("DELETE FROM cart_table where creation_date =:productId")
    suspend fun clear(productId: String)

    @Query("SELECT * FROM cart_table")
    suspend fun getCartList(): List<CartProduct>

    @Query("SELECT SUM(price) FROM cart_table")
    suspend fun getTotalPrice(): Double

    @Query("DELETE FROM cart_table")
    suspend fun clearList()
}
