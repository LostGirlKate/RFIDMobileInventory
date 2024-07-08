package lost.girl.rfidmobileinventory.data.repository

import lost.girl.rfidmobileinventory.data.storage.InventoryStorage
import lost.girl.rfidmobileinventory.data.storage.roomdb.ClearDataBase
import lost.girl.rfidmobileinventory.data.storage.roomdb.MainDataBase
import lost.girl.rfidmobileinventory.domain.models.InventoryInfoModel
import lost.girl.rfidmobileinventory.domain.models.InventoryItemForDetailFullModel
import lost.girl.rfidmobileinventory.domain.models.InventoryItemForExportModel
import lost.girl.rfidmobileinventory.domain.models.InventoryItemForListModel
import lost.girl.rfidmobileinventory.domain.models.InventoryItemForScanningModel
import lost.girl.rfidmobileinventory.domain.models.InventoryItemFullModel
import lost.girl.rfidmobileinventory.domain.models.InventoryLocationFullModel
import lost.girl.rfidmobileinventory.domain.repository.InventoryRepository

class InventoryRepositoryImpl(
    private val inventoryStorage: InventoryStorage,
) : InventoryRepository {

    override suspend fun insertInventoryLocation(location: InventoryLocationFullModel): Boolean {
        val inventoryLocation = location.toInventoryLocation()
        inventoryStorage.insertInventoryLocation(inventoryLocation)
        return true
    }

    override fun getAllLocationList(): List<InventoryLocationFullModel> {
        return inventoryStorage.getAllLocationList().map { it.toInventoryLocationFullModel() }
    }

    override fun getAllInventoryFullList(): List<InventoryItemFullModel> {
        return inventoryStorage.getAllInventoryItemList().map { it.toInventoryItemFullModel() }
    }

    override fun insertManyInventoryItem(items: List<InventoryItemFullModel>): List<Long> {
        return inventoryStorage.insertManyInventoryItem(items.map { it.toInventoryItem() })
    }

    override fun getInventoryItemByLocationID(locationID: Int): List<InventoryItemForListModel> {
        return inventoryStorage.getInventoryItemByLocationID(locationID)
            .map { it.toInventoryItemModelForList() }
    }

    override fun getInventoryItemsCounts(locationID: Int): InventoryInfoModel {
        return inventoryStorage.getInventoryItemsCounts(locationID)
            .map { it.toInventoryInfoModel() }.first()
    }

    override fun getAllInventoryItemForExport(): List<InventoryItemForExportModel> {
        return inventoryStorage.getAllInventoryItemList().mapIndexed { index, inventoryItem ->
            inventoryItem.toInventoryItemForExportModel(index + 1)
        }
    }

    override suspend fun clearAll(dataBase: MainDataBase): Boolean {
        ClearDataBase.execute(dataBase)
        return true
    }

    override fun getInventoryItemDetail(id: Int): InventoryItemForDetailFullModel {
        return inventoryStorage.getInventoryItemDetail(id).toInventoryItemFullModelForDetail()
    }

    override fun updateLocationInventoryItemByID(locationID: Int, location: String, id: Int) {
        inventoryStorage.updateLocationInventoryItemByID(locationID, location, id)
    }

    override fun getAllInventoryItemListForRfidScanning(): List<InventoryItemForScanningModel> {
        return inventoryStorage.getAllInventoryItemList()
            .map { it.toInventoryItemForScanningModel() }
    }

    override fun resetLocationInventoryItemByID(id: Int) {
        inventoryStorage.resetLocationInventoryItemByID(id)
    }
}
