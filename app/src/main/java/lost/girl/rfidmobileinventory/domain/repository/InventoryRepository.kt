package lost.girl.rfidmobileinventory.domain.repository

import lost.girl.rfidmobileinventory.data.storage.roomdb.MainDataBase
import lost.girl.rfidmobileinventory.domain.models.InventoryInfoModel
import lost.girl.rfidmobileinventory.domain.models.InventoryItemForDetailFullModel
import lost.girl.rfidmobileinventory.domain.models.InventoryItemForExportModel
import lost.girl.rfidmobileinventory.domain.models.InventoryItemForListModel
import lost.girl.rfidmobileinventory.domain.models.InventoryItemForScanningModel
import lost.girl.rfidmobileinventory.domain.models.InventoryItemFullModel
import lost.girl.rfidmobileinventory.domain.models.InventoryLocationFullModel

interface InventoryRepository {

    // insert ТМЦ
    suspend fun insertInventoryLocation(location: InventoryLocationFullModel): Boolean

    // общий список местопожений (справочник)
    fun getAllLocationList(): List<InventoryLocationFullModel>

    // общий список ТМЦ
    fun getAllInventoryFullList(): List<InventoryItemFullModel>

    // insert ТМЦ list
    fun insertManyInventoryItem(items: List<InventoryItemFullModel>): List<Long>

    // список ТМЦ по местоположению
    fun getInventoryItemByLocationID(locationID: Int): List<InventoryItemForListModel>

    // Информация об общем количестве объектов для инвентаризации
    fun getInventoryItemsCounts(locationID: Int): InventoryInfoModel

    // общий список ТМЦ для экспорта в Excel
    fun getAllInventoryItemForExport(): List<InventoryItemForExportModel>

    // очистка DB
    suspend fun clearAll(dataBase: MainDataBase): Boolean

    // Информация об 1 ТМЦ по id
    fun getInventoryItemDetail(id: Int): InventoryItemForDetailFullModel

    // update актуального местоположения ТМЦ по id
    fun updateLocationInventoryItemByID(locationID: Int, location: String, id: Int)

    // общий список ТМЦ для сканирования
    fun getAllInventoryItemListForRfidScanning(): List<InventoryItemForScanningModel>
}
