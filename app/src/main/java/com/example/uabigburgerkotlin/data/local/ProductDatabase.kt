package bigburger.ua.com.uabigburger.data.local

import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.content.Context
import com.example.uabigburgerkotlin.R
import com.example.uabigburgerkotlin.data.local.dao.ProductsDAO
import com.example.uabigburgerkotlin.data.local.model.Product

/**
 * Created by wessim.trimeche on 16/02/19.
 */
@Database(entities = [Product::class], version = 1, exportSchema = false)
abstract class ProductDatabase : RoomDatabase() {
    abstract fun productsDAO(): ProductsDAO

    companion object {
        lateinit var productDatabase: ProductDatabase
        fun getRoomDatabaseInstance(context: Context): ProductDatabase {

            productDatabase = Room.databaseBuilder(
                context,
                ProductDatabase::class.java,
                context.getString(R.string.product_database_name)
            ).build()

            return productDatabase
        }
    }
}