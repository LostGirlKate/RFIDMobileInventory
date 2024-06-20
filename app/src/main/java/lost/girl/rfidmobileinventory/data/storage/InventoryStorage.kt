package lost.girl.rfidmobileinventory.data.storage

import lost.girl.rfidmobileinventory.data.storage.models.InventoryCounts
import lost.girl.rfidmobileinventory.data.storage.models.InventoryItem
import lost.girl.rfidmobileinventory.data.storage.models.InventoryLocation

interface InventoryStorage {

    // insert ТМЦ
    suspend fun insertInventoryLocation(location: InventoryLocation)

    // общий список местопожений (справочник)
    fun getAllLocationList(): List<InventoryLocation>

    // общий список ТМЦ
    fun getAllInventoryItemList(): List<InventoryItem>

    // insert ТМЦ list
    fun insertManyInventoryItem(items: List<InventoryItem>): List<Long>

    // список ТМЦ по местоположению
    fun getInventoryItemByLocationID(locationID: Int): List<InventoryItem>

    // Информация об общем количестве объектов для инвентаризации
    fun getInventoryItemsCounts(locationID: Int): List<InventoryCounts>

    // Информация об 1 ТМЦ по id
    fun getInventoryItemDetail(id: Int): InventoryItem

    // update актуального местоположения ТМЦ по id
    fun updateLocationInventoryItemByID(locationID: Int, location: String, id: Int)
}
