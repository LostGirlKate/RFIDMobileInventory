package lost.girl.rfidmobileinventory.data.storage.roomdb

import androidx.room.Database
import androidx.room.RoomDatabase
import lost.girl.rfidmobileinventory.data.storage.models.InventoryItem
import lost.girl.rfidmobileinventory.data.storage.models.InventoryLocation

@Database(entities = [InventoryLocation::class, InventoryItem::class], version = 1)
abstract class MainDataBase : RoomDatabase() {

    abstract fun getDao(): Dao
}
