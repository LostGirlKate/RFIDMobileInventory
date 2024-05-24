package lost.girl.rfidmobileinventory.domain.models

data class InventoryItemDetailItem(
    val paraName: String,
    val value: String,
    val isStatus: Boolean = false,
    val statusColor: Int = 0
)
