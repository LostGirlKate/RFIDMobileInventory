package lost.girl.rfidmobileinventory.domain.repository

import android.content.Context
import lost.girl.rfidmobileinventory.domain.models.InventoryInfoModel
import lost.girl.rfidmobileinventory.domain.models.InventoryItemForDetailFullModel
import lost.girl.rfidmobileinventory.domain.models.InventoryItemForExportModel
import lost.girl.rfidmobileinventory.domain.models.InventoryItemForListModel
import lost.girl.rfidmobileinventory.domain.models.InventoryItemForScanningModel
import lost.girl.rfidmobileinventory.domain.models.InventoryItemFullModel
import lost.girl.rfidmobileinventory.domain.models.InventoryLocationFullModel

interface InventoryRepository {
    suspend fun updateInventoryItem(item: InventoryItemFullModel): Boolean
    suspend fun insertInventoryLocation(location: InventoryLocationFullModel): Boolean
    fun getAllLocationList(): List<InventoryLocationFullModel>
    fun getAllInventoryItemList(): List<InventoryItemForListModel>
    fun getAllInventoryFullList(): List<InventoryItemFullModel>
    fun insertInventoryItem(item: InventoryItemFullModel)
    fun insertManyInventoryItem(items: List<InventoryItemFullModel>): List<Long>
    fun getInventoryItemByLocationID(locationID: Int): List<InventoryItemForListModel>
    fun getInventoryItemsCounts(locationID: Int): InventoryInfoModel
    fun getAllInventoryItemForExport(): List<InventoryItemForExportModel>
    suspend fun clearAll(context: Context): Boolean
    fun getInventoryItemDetail(id: Int): InventoryItemForDetailFullModel
    fun updateLocationInventoryItem(locationID: Int, location: String, items: List<String>)
    fun updateLocationInventoryItemByID(locationID: Int, location: String, id: Int)
    fun getAllInventoryItemListForRfidScanning(): List<InventoryItemForScanningModel>
}