package lost.girl.rfidmobileinventory.ui.rfidscan

import lost.girl.rfidmobileinventory.domain.models.InventoryInfoModel
import lost.girl.rfidmobileinventory.domain.models.InventoryItemForListModel

data class RfidScannerViewState(
    val isScanningStart: Boolean = false,
    val canBackPress: Boolean = true,
    val startRfidScanningButtonVisible: Boolean = true,
    val panelSettingsVisible: Boolean = true,
    val stopScanningButtonVisible: Boolean = false,
    val scannerPowerValue: Int = 50,
    val currentLocation: Int = 0,
    val currentLocationName: String = "",
    val inventoryItems: List<InventoryItemForListModel> = listOf(),
    val inventoryState: InventoryInfoModel = InventoryInfoModel(),
    val inventoryItemsFullRFIDList: List<Pair<Int, String>> = listOf()
)
