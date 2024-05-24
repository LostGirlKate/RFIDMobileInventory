package lost.girl.rfidmobileinventory.domain.models

data class InventoryItemFullModel(
    val id: Int?,
    val inventoryNum: String,
    var managerName: String,
    var locationID: Int?,
    var location: String,
    var type: String,
    var model: String,
    var serialNum: String,
    var shipmentNum: String,
    val rfidCardNum: String,
    var actualLocationID: Int?,
    var actualLocation: String?
)
