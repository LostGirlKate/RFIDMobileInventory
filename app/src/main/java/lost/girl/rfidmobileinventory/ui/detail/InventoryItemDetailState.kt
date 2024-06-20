package lost.girl.rfidmobileinventory.ui.detail

import lost.girl.rfidmobileinventory.domain.models.InventoryItemDetailModel

data class InventoryItemDetailState(
    // Список параметров ТМЦ со значениями
    val details: List<InventoryItemDetailModel> = listOf(),
)
