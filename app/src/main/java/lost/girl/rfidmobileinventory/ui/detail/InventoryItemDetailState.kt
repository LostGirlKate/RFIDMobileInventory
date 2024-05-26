package lost.girl.rfidmobileinventory.ui.detail

import lost.girl.rfidmobileinventory.domain.models.InventoryItemDetailModel

data class InventoryItemDetailState(
    val details: List<InventoryItemDetailModel> = listOf()
)
