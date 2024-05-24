package lost.girl.rfidmobileinventory.data.repository

import android.content.Context
import lost.girl.rfidmobileinventory.data.storage.InventoryStorage
import lost.girl.rfidmobileinventory.data.storage.models.InventoryCounts
import lost.girl.rfidmobileinventory.data.storage.models.InventoryItem
import lost.girl.rfidmobileinventory.data.storage.models.InventoryLocation
import lost.girl.rfidmobileinventory.data.storage.roomdb.ClearDataBase
import lost.girl.rfidmobileinventory.domain.models.InventoryInfoModel
import lost.girl.rfidmobileinventory.domain.models.InventoryItemForExportModel
import lost.girl.rfidmobileinventory.domain.models.InventoryItemFullModel
import lost.girl.rfidmobileinventory.domain.models.InventoryItemFullModelForDetail
import lost.girl.rfidmobileinventory.domain.models.InventoryItemModelForList
import lost.girl.rfidmobileinventory.domain.models.InventoryItemState
import lost.girl.rfidmobileinventory.domain.models.InventoryLocationFullModel
import lost.girl.rfidmobileinventory.domain.models.InventoryState
import lost.girl.rfidmobileinventory.domain.repository.InventoryRepository
import java.text.DecimalFormat

class InventoryStorageImpl(
    private val inventoryStorage: InventoryStorage
) : InventoryRepository {

    override suspend fun updateInventoryItem(item: InventoryItemFullModel): Boolean {
        val inventoryItem = mapToInventoryItem(item)
        inventoryStorage.updateInventoryItem(inventoryItem)
        return true
    }

    override suspend fun insertInventoryLocation(location: InventoryLocationFullModel): Boolean {
        val inventoryLocation = mapToInventoryLocation(location)
        inventoryStorage.insertInventoryLocation(inventoryLocation)
        return true
    }

    override fun getAllLocationList(): List<InventoryLocationFullModel> {
        return inventoryStorage.getAllLocationList().map { mapToInventoryLocationFullModel(it) }
    }

    override fun getAllInventoryItemList(): List<InventoryItemModelForList> {
        TODO("Not yet implemented")
    }

    override fun getAllInventoryFullList(): List<InventoryItemFullModel> {
        return inventoryStorage.getAllInventoryItemList().map { mapToInventoryItemFullModel(it) }
    }

    override fun insertInventoryItem(item: InventoryItemFullModel) {
        TODO("Not yet implemented")
    }

    override fun insertManyInventoryItem(items: List<InventoryItemFullModel>): List<Long> {
        return inventoryStorage.insertManyInventoryItem(items.map { mapToInventoryItem(it) })
    }

    override fun getInventoryItemByLocationID(locationID: Int): List<InventoryItemModelForList> {
        return inventoryStorage.getInventoryItemByLocationID(locationID)
            .map { mapToInventoryItemModelForList(it) }
    }

    override fun getInventoryItemsCounts(locationID: Int): InventoryInfoModel {
        return inventoryStorage.getInventoryItemsCounts(locationID)
            .map { mapToInventoryInfoModel(it) }.first()
    }

    override fun getAllInventoryItemForExport(): List<InventoryItemForExportModel> {
        return inventoryStorage.getAllInventoryItemList().mapIndexed { index, inventoryItem ->
            mapToInventoryItemForExportModel(
                inventoryItem,
                index + 1
            )
        }
    }

    override suspend fun clearAll(context: Context): Boolean {
        ClearDataBase.execute(context)
        return true
    }

    override fun getInventoryItemDetail(id: Int): InventoryItemFullModelForDetail {
        return mapToInventoryItemFullModelForDetail(inventoryStorage.getInventoryItemDetail(id))
    }

    override fun updateLocationInventoryItem(
        locationID: Int,
        location: String,
        items: List<String>
    ) {
        inventoryStorage.updateLocationInventoryItem(locationID, location, items)
    }

    override fun updateLocationInventoryItemByID(locationID: Int, location: String, id: Int) {
        inventoryStorage.updateLocationInventoryItemByID(locationID, location, id)
    }

    override fun getAllInventoryItemListForRfidScanning(): List<Pair<Int, String>> {
        return inventoryStorage.getAllInventoryItemList().map { Pair(it.id ?: 0, it.rfidCardNum) }
    }

    private fun mapToInventoryItem(item: InventoryItemFullModel): InventoryItem {
        return InventoryItem(
            id = item.id,
            inventoryNum = item.inventoryNum,
            managerName = item.managerName,
            locationID = item.locationID,
            location = item.location,
            type = item.type,
            model = item.model,
            serialNum = item.serialNum,
            shipmentNum = item.shipmentNum,
            rfidCardNum = item.rfidCardNum,
            actualLocationID = item.actualLocationID,
            actualLocation = item.actualLocation
        )
    }

    private fun mapToInventoryItemForExportModel(
        item: InventoryItem,
        num: Int
    ): InventoryItemForExportModel {
        return InventoryItemForExportModel(
            rowNum = num.toString(),
            inventoryNum = item.inventoryNum,
            managerName = item.managerName,
            location = item.location,
            type = item.type,
            model = item.model,
            serialNum = item.serialNum,
            shipmentNum = item.shipmentNum,
            rfidCardNum = item.rfidCardNum,
            actualLocation = item.actualLocation.toString()
        )
    }

    private fun mapToInventoryItemFullModelForDetail(item: InventoryItem): InventoryItemFullModelForDetail {
        val state = when (item.actualLocationID) {
            null -> "Не найдено"
            item.locationID -> "Найдено"
            else -> "Найдено не на своем месте"
        }
        return InventoryItemFullModelForDetail(
            id = item.id,
            inventoryNum = item.inventoryNum,
            managerName = item.managerName,
            location = item.location,
            type = item.type,
            model = item.model,
            serialNum = item.serialNum,
            shipmentNum = item.shipmentNum,
            rfidCardNum = item.rfidCardNum,
            actualLocation = item.actualLocation,
            status = state
        )
    }

    private fun mapToInventoryItemModelForList(item: InventoryItem): InventoryItemModelForList {
        val state = when (item.actualLocationID) {
            null -> InventoryItemState.STATE_NOT_FOUND
            item.locationID -> InventoryItemState.STATE_FOUND
            else -> InventoryItemState.STATE_FOUND_IN_WRONG_PLACE
        }
        return InventoryItemModelForList(
            id = item.id,
            model = item.model,
            state = state,
            inventoryNum = item.inventoryNum,
            location = item.actualLocation ?: item.location
        )
    }


    private fun mapToInventoryItemFullModel(item: InventoryItem): InventoryItemFullModel {
        return InventoryItemFullModel(
            id = item.id,
            inventoryNum = item.inventoryNum,
            managerName = item.managerName,
            locationID = item.locationID,
            location = item.location,
            type = item.type,
            model = item.model,
            serialNum = item.serialNum,
            shipmentNum = item.shipmentNum,
            rfidCardNum = item.rfidCardNum,
            actualLocationID = item.actualLocationID,
            actualLocation = item.actualLocation
        )
    }

    private fun mapToInventoryLocation(location: InventoryLocationFullModel): InventoryLocation {
        return InventoryLocation(
            id = location.id,
            name = location.name
        )
    }

    private fun mapToInventoryLocationFullModel(location: InventoryLocation): InventoryLocationFullModel {
        return InventoryLocationFullModel(
            id = location.id,
            name = location.name
        )
    }

    private fun mapToInventoryInfoModel(inventoryCounts: InventoryCounts): InventoryInfoModel {
        val state = when (inventoryCounts.countAll) {
            0 -> InventoryState.STATE_NOT_START
            (inventoryCounts.countFound + inventoryCounts.countFoundInWrongPlace) -> InventoryState.STATE_READY
            else -> InventoryState.STATE_WORK
        }
        val percentFound = if (inventoryCounts.countAll > 0)
            ((inventoryCounts.countFound + inventoryCounts.countFoundInWrongPlace)  / (inventoryCounts.countAll.toDouble()) * 100).toInt() else 0

        val percentFoundString = "$percentFound%"

        return InventoryInfoModel(
            countAllString = inventoryCounts.countAll.formatterToString(),
            countFoundString = inventoryCounts.countFound.formatterToString(),
            countNotFoundString = inventoryCounts.countNotFound.formatterToString(),
            countFoundInWrongPlaceString = inventoryCounts.countFoundInWrongPlace.formatterToString(),
            percentFound = percentFound,
            percentFoundString = percentFoundString,
            inventoryState = state
        )
    }

    private fun Int.formatterToString() =
        DecimalFormat("#,###")
            .format(this)
            .replace(",", " ")
}

