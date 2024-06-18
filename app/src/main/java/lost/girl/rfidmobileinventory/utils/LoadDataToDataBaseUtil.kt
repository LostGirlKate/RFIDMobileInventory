package lost.girl.rfidmobileinventory.utils

import lost.girl.rfidmobileinventory.domain.models.InventoryItemFullModel
import lost.girl.rfidmobileinventory.domain.models.InventoryLocationFullModel
import lost.girl.rfidmobileinventory.domain.repository.InventoryRepository
import timber.log.Timber

const val COLUMN_INDEX_INVENTORY_NUM = 1
const val COLUMN_INDEX_MANAGER_NAME = 2
const val COLUMN_INDEX_LOADED_LOCATION = 3
const val COLUMN_INDEX_TYPE = 4
const val COLUMN_INDEX_MODEL = 5
const val COLUMN_INDEX_SERIAL_NUM = 6
const val COLUMN_INDEX_SHIPMENT_NUM = 7
const val COLUMN_INDEX_RFID_CARD_NUM = 8
object LoadDataToDataBaseUtil {
    suspend fun load(
        inventoryRepository: InventoryRepository,
        data: Array<Array<String>>
    ): Boolean {
        var model: String
        var rfidCardNum: String
        var loadedLocationID: Int?
        var loadedLocation: String
        var inventoryNum: String
        var managerName: String
        var type: String
        var serialNum: String
        var shipmentNum: String

        var result = true
        val itemList: ArrayList<InventoryItemFullModel> = arrayListOf()
        try {
            val allItem = inventoryRepository.getAllInventoryFullList()
            var allLocation = inventoryRepository.getAllLocationList()
            for ((index, array) in data.withIndex()) {
                if (index > 0) {
                    inventoryNum = array[COLUMN_INDEX_INVENTORY_NUM]
                    managerName = array[COLUMN_INDEX_MANAGER_NAME]
                    loadedLocation = array[COLUMN_INDEX_LOADED_LOCATION]
                    type = array[COLUMN_INDEX_TYPE]
                    model = array[COLUMN_INDEX_MODEL]
                    serialNum = array[COLUMN_INDEX_SERIAL_NUM]
                    shipmentNum = array[COLUMN_INDEX_SHIPMENT_NUM]
                    rfidCardNum = array[COLUMN_INDEX_RFID_CARD_NUM]

                    val noneLocation = allLocation.none { it.name == loadedLocation }
                    loadedLocationID = if (noneLocation) {
                        inventoryRepository.insertInventoryLocation(
                            InventoryLocationFullModel(null, loadedLocation)
                        )
                        allLocation = inventoryRepository.getAllLocationList()
                        allLocation.first { it.name == loadedLocation }.id
                    } else {
                        allLocation.first { it.name == loadedLocation }.id
                    }

                    if (allItem.none { it.rfidCardNum == rfidCardNum }) {
                        itemList.add(
                            InventoryItemFullModel(
                                id = null,
                                model = model,
                                rfidCardNum = rfidCardNum,
                                actualLocation = null,
                                locationID = loadedLocationID!!,
                                actualLocationID = null,
                                location = loadedLocation,
                                inventoryNum = inventoryNum,
                                managerName = managerName,
                                type = type,
                                serialNum = serialNum,
                                shipmentNum = shipmentNum
                            )
                        )
                    }
                }
            }
            inventoryRepository.insertManyInventoryItem(itemList)
        } catch (ex: Exception) {
            Timber.tag("LoadDataToDataBaseUtil").e(ex)
            result = false
        }

        return result
    }
}
