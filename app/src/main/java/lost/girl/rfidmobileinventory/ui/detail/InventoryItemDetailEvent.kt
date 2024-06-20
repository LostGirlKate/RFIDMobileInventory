package lost.girl.rfidmobileinventory.ui.detail

import lost.girl.rfidmobileinventory.domain.models.InventoryItemState

sealed class InventoryItemDetailEvent {
    // Открытие детализации ТМЦ (параметры id ТМЦ и текущее состояние ТМЦ)
    data class OpenDetails(val itemId: Int, val state: InventoryItemState) :
        InventoryItemDetailEvent()
}
