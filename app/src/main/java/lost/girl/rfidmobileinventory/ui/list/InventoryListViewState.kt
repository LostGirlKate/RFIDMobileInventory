package lost.girl.rfidmobileinventory.ui.list

import lost.girl.rfidmobileinventory.domain.models.InventoryInfoModel
import lost.girl.rfidmobileinventory.domain.models.InventoryItemForListModel
import lost.girl.rfidmobileinventory.domain.models.InventoryLocationFullModel

data class InventoryListViewState(
    val locations: List<InventoryLocationFullModel> = listOf(),
    val inventoryItems: List<InventoryItemForListModel> = listOf(),
    val inventoryState: InventoryInfoModel = InventoryInfoModel(),
    val currentLocationID: Int = -1
)
