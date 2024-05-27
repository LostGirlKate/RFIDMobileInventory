package lost.girl.rfidmobileinventory.domain.models

import lost.girl.rfidmobileinventory.data.storage.models.InventoryItem

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
) {
    fun toInventoryItem() = InventoryItem(
        id = this.id,
        inventoryNum = this.inventoryNum,
        managerName = this.managerName,
        locationID = this.locationID,
        location = this.location,
        type = this.type,
        model = this.model,
        serialNum = this.serialNum,
        shipmentNum = this.shipmentNum,
        rfidCardNum = this.rfidCardNum,
        actualLocationID = this.actualLocationID,
        actualLocation = this.actualLocation
    )
}
