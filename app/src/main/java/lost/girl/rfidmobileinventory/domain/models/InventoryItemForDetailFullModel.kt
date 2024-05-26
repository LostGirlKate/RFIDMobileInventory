package lost.girl.rfidmobileinventory.domain.models

data class InventoryItemForDetailFullModel(
    val id: Int?,
    val inventoryNum: String,
    var managerName: String,
    var location: String,
    var type: String,
    var model: String,
    var serialNum: String,
    var shipmentNum: String,
    val rfidCardNum: String,
    var actualLocation: String?,
    var status: String
)