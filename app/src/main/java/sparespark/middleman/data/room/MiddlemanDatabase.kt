package sparespark.middleman.data.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import sparespark.middleman.data.room.brand.BrandDao
import sparespark.middleman.data.room.brand.RoomBrand
import sparespark.middleman.data.room.cart.CartDao
import sparespark.middleman.data.room.cart.RoomCartProduct
import sparespark.middleman.data.room.product.ProductDao
import sparespark.middleman.data.room.product.RoomProduct
import sparespark.middleman.data.room.user.RoomUser
import sparespark.middleman.data.room.user.UserDao

private const val DATABASE = "middleman_db"

@Database(
    entities = [
        RoomUser::class,
        RoomProduct::class,
        RoomBrand::class,
        RoomCartProduct::class,
    ], version = 1, exportSchema = false
)
@TypeConverters(DataConverter::class)
abstract class MiddlemanDatabase : RoomDatabase() {

    abstract fun userDao(): UserDao
    abstract fun productDao(): ProductDao
    abstract fun brandDao(): BrandDao
    abstract fun cartDao(): CartDao

    companion object {

        @Volatile
        private var instance: MiddlemanDatabase? = null

        fun getInstance(context: Context): MiddlemanDatabase {
            return instance ?: synchronized(this) {
                instance
                    ?: buildDatabase(context).also { instance = it }
            }
        }

        private fun buildDatabase(context: Context): MiddlemanDatabase {
            return Room.databaseBuilder(context, MiddlemanDatabase::class.java, DATABASE)
                .build()
        }
    }
}