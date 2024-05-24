package lost.girl.rfidmobileinventory.data.storage.roomdb

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import lost.girl.rfidmobileinventory.data.storage.models.InventoryItem
import lost.girl.rfidmobileinventory.data.storage.models.InventoryLocation

@Database(entities = [InventoryLocation::class, InventoryItem::class], version = 1)
abstract class MainDataBase: RoomDatabase() {

    abstract fun getDao(): Dao

    companion object{
        @Volatile
        private var INSTANCE: MainDataBase? = null
        fun getDataBase(context: Context): MainDataBase {
            return INSTANCE ?: synchronized(this){
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    MainDataBase::class.java,
                    "inventory.db"
                )
                    .allowMainThreadQueries()
                    .build()
                instance
            }
        }
    }

}