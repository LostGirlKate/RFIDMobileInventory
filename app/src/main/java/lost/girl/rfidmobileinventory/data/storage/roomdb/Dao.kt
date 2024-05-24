package lost.girl.rfidmobileinventory.data.storage.roomdb

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow
import lost.girl.rfidmobileinventory.data.storage.InventoryStorage
import lost.girl.rfidmobileinventory.data.storage.models.InventoryCounts
import lost.girl.rfidmobileinventory.data.storage.models.InventoryItem
import lost.girl.rfidmobileinventory.data.storage.models.InventoryLocation

@Dao
interface Dao : InventoryStorage {

    @Update
    override suspend fun updateInventoryItem(item: InventoryItem)


    @Query("select * from inventory_location order by name")
    override fun getAllInventoryLocation(): Flow<List<InventoryLocation>>

    @Insert
    override suspend fun insertInventoryLocation(location: InventoryLocation)

    @Query(" select -1 as id, 'Все' as name union select * from inventory_location ")
    override fun getAllLocationList(): List<InventoryLocation>


    @Query("select * from inventory_item")
    override fun getAllInventoryItem(): Flow<List<InventoryItem>>

    @Query("select * from inventory_item")
    override fun getAllInventoryItemList(): List<InventoryItem>

    @Insert
    override suspend fun insertInventoryItem(item: InventoryItem)

    @Insert
    override fun insertManyInventoryItem(items: List<InventoryItem>): List<Long>

    @Query("select * from inventory_item where (location_id LIKE :locationID and actual_location_id is null) or actual_location_id like :locationID or :locationID = -1 order by inventory_num")
    override fun getInventoryItemByLocationID(locationID: Int): List<InventoryItem>


    @Query("select count(1) countAll, count(case when actual_location_id is not null and actual_location_id = location_id then 1 else null end) countFound, count(case when actual_location_id is null then 1 else null end) countNotFound , count(case when actual_location_id is not null and actual_location_id<>location_id then 1 else null end) countFoundInWrongPlace from inventory_item where (location_id LIKE :locationID and actual_location_id is null) or actual_location_id like :locationID or :locationID = -1")
    override fun getInventoryItemsCounts(locationID: Int): List<InventoryCounts>

    @Query("select * from inventory_item where  id like :id")
    override fun getInventoryItemDetail(id: Int): InventoryItem

    @Query("UPDATE inventory_item SET actual_location_id = :locationID , actual_location = :location WHERE id in (:items) ")
    override fun updateLocationInventoryItem(
        locationID: Int,
        location: String,
        items: List<String>
    )
    @Query("UPDATE inventory_item SET actual_location_id = :locationID , actual_location = :location WHERE id = :id ")
    override fun updateLocationInventoryItemByID(
        locationID: Int,
        location: String,
        id: Int
    )

}
