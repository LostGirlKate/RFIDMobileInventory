package lost.girl.rfidmobileinventory.data.storage

import kotlinx.coroutines.flow.Flow
import lost.girl.rfidmobileinventory.data.storage.models.InventoryCounts
import lost.girl.rfidmobileinventory.data.storage.models.InventoryItem
import lost.girl.rfidmobileinventory.data.storage.models.InventoryLocation

interface InventoryStorage {
    suspend fun updateInventoryItem(item: InventoryItem)
    fun getAllInventoryLocation(): Flow<List<InventoryLocation>>
    suspend fun insertInventoryLocation(location: InventoryLocation)
    fun getAllLocationList(): List<InventoryLocation>
    fun getAllInventoryItemList(): List<InventoryItem>
    fun insertManyInventoryItem(items: List<InventoryItem>): List<Long>
    fun getInventoryItemByLocationID(locationID: Int): List<InventoryItem>
    fun getInventoryItemsCounts(locationID: Int): List<InventoryCounts>
    fun getInventoryItemDetail(id: Int): InventoryItem
    fun updateLocationInventoryItem(locationID: Int, location: String, items: List<String>)
    fun updateLocationInventoryItemByID(locationID: Int, location: String, id: Int)
}
