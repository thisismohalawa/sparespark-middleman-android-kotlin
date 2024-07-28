package sparespark.middleman.model.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

private const val DATABASE = "middleman_db"

@Database(
    entities = [
        RoomUser::class,
        RoomProduct::class,
        CartProduct::class,
        RoomBrand::class
    ], version = 1, exportSchema = false
)
@TypeConverters(DataConverter::class)
abstract class XDatabase : RoomDatabase() {

    abstract fun userDao(): UserDao
    abstract fun productDao(): ProductDao
    abstract fun brandDao(): BrandDao
    abstract fun cartDao(): CartDao

    companion object {

        @Volatile
        private var instance: XDatabase? = null

        fun getInstance(context: Context): XDatabase {
            return instance ?: synchronized(this) {
                instance
                    ?: buildDatabase(context).also { instance = it }
            }
        }

        private fun buildDatabase(context: Context): XDatabase {
            return Room.databaseBuilder(context, XDatabase::class.java, DATABASE)
                .build()
        }
    }
}