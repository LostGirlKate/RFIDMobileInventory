package lost.girl.rfidmobileinventory.domain.models

import lost.girl.rfidmobileinventory.data.storage.models.InventoryItem

// Модель данных ТМЦ с полным списком параметров
data class InventoryItemFullModel(
    val id: Int?,
    val inventoryNum: String,
    val managerName: String,
    val locationID: Int?,
    val location: String,
    val type: String,
    val model: String,
    val serialNum: String,
    val shipmentNum: String,
    val rfidCardNum: String,
    val actualLocationID: Int?,
    val actualLocation: String?,
    val prevLocationID: Int?,
    val prevLocation: String?,
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
        actualLocation = this.actualLocation,
        prevLocationID = this.prevLocationID,
        prevLocation = this.prevLocation
    )
}
