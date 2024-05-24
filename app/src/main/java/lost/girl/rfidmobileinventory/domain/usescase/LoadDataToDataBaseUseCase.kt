package lost.girl.rfidmobileinventory.domain.usescase

import android.util.Log
import lost.girl.rfidmobileinventory.domain.models.InventoryItemFullModel
import lost.girl.rfidmobileinventory.domain.models.InventoryLocationFullModel
import lost.girl.rfidmobileinventory.domain.repository.InventoryRepository

class LoadDataToDataBaseUseCase(
    private val inventoryRepository: InventoryRepository
) {
    suspend fun execute(data: Array<Array<String>>): Boolean {
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
            Log.d("InvMobRFID", "Start")

            val allItem = inventoryRepository.getAllInventoryFullList()
            var allLocation = inventoryRepository.getAllLocationList()
            for ((index, array) in data.withIndex()) {
                if (index > 0) {
                    inventoryNum = array[1]
                    managerName = array[2]
                    loadedLocation = array[3]
                    type = array[4]
                    model = array[5]
                    serialNum = array[6]
                    shipmentNum = array[7]
                    rfidCardNum = array[8]

                    val noneLocation = allLocation.none { it.name == loadedLocation }
                    loadedLocationID = if (noneLocation) {
                        inventoryRepository.insertInventoryLocation(
                            InventoryLocationFullModel(null, loadedLocation)
                        )
                        Log.d("InvMobRFID", loadedLocation)
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
            Log.d("InvMobRFID", "Finish")
        } catch (ex: Exception) {
            Log.d("InvMobRFID", ex.message.toString())
            result = false
        }

        return result
    }
}