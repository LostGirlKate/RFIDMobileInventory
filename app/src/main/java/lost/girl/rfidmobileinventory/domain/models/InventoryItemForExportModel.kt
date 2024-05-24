package lost.girl.rfidmobileinventory.domain.models

data class InventoryItemForExportModel(
    val rowNum: String,
    val inventoryNum: String,
    var managerName: String,
    var location: String,
    var type: String,
    var model: String,
    var serialNum: String,
    var shipmentNum: String,
    val rfidCardNum: String,
    var actualLocation: String
) {
    fun toListOfString(): List<String> {
        val resultList =
            "$rowNum,$inventoryNum,$managerName,$location,$type,$model,$serialNum,$shipmentNum,$rfidCardNum,${if (actualLocation == "null") "" else actualLocation}".split(
                ','
            )
        return resultList
    }
}
