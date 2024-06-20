package lost.girl.rfidmobileinventory.ui.list

import lost.girl.rfidmobileinventory.domain.models.InventoryInfoModel
import lost.girl.rfidmobileinventory.domain.models.InventoryItemForListModel
import lost.girl.rfidmobileinventory.domain.models.InventoryLocationFullModel

data class InventoryListViewState(
    // Список местоположений
    val locations: List<InventoryLocationFullModel> = listOf(),
    // Список ТМЦ
    val inventoryItems: List<InventoryItemForListModel> = listOf(),
    // Состояние инвентаризации
    val inventoryState: InventoryInfoModel = InventoryInfoModel(),
    // Фильтр по местоположению - актуальное значение
    val currentLocationID: Int = -1,
)
