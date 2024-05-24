package lost.girl.rfidmobileinventory.ui.detail

import lost.girl.rfidmobileinventory.domain.models.InventoryItemDetailItem

data class InventoryItemDetailState(
    val details: List<InventoryItemDetailItem> = listOf()
)
