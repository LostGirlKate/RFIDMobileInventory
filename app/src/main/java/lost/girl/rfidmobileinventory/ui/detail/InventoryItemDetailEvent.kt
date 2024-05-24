package lost.girl.rfidmobileinventory.ui.detail

import lost.girl.rfidmobileinventory.domain.models.InventoryItemState

sealed class InventoryItemDetailEvent {
    data class OpenDetails(val itemId: Int, val state: InventoryItemState): InventoryItemDetailEvent()
}