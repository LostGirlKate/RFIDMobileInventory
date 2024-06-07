package lost.girl.rfidmobileinventory.domain.models

data class InventoryItemForScanningModel(
    val id: Int?,
    val shipmentNum: String,
    val rfidCardNum: String
)
