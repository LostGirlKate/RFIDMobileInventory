package lost.girl.rfidmobileinventory.domain.models

data class InventoryItemModelForList(
    val id: Int?,
    val model: String,
    val state: InventoryItemState,
    val location: String,
    val inventoryNum: String
)
