package lost.girl.rfidmobileinventory.ui.main

import lost.girl.rfidmobileinventory.R
import lost.girl.rfidmobileinventory.domain.models.InventoryInfoModel
import lost.girl.rfidmobileinventory.domain.models.InventoryState

data class InventoryMainViewState(
    val inventoryState: InventoryInfoModel
) {
    val mainInfoBlockVisible = (inventoryState.inventoryState != InventoryState.STATE_NOT_START)
    val openInventoryButtonVisible =
        (inventoryState.inventoryState != InventoryState.STATE_NOT_START)
    val closeInventoryButtonVisible =
        (inventoryState.inventoryState != InventoryState.STATE_NOT_START)
    val exportButtonVisible = (inventoryState.inventoryState != InventoryState.STATE_NOT_START)
    val loadFromFileButtonVisible =
        (inventoryState.inventoryState == InventoryState.STATE_NOT_START)
    val loadingWarningTextVisible =
        (inventoryState.inventoryState == InventoryState.STATE_NOT_START)
    val progressPercentTextColor =
        if (inventoryState.inventoryState == InventoryState.STATE_READY) {
            R.color.green_percent_text
        } else {
            R.color.white
        }
}
