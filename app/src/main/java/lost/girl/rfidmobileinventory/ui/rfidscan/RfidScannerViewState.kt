package lost.girl.rfidmobileinventory.ui.rfidscan

import lost.girl.rfidmobileinventory.data.readers.ReaderType
import lost.girl.rfidmobileinventory.domain.models.InventoryInfoModel
import lost.girl.rfidmobileinventory.domain.models.InventoryItemForListModel
import lost.girl.rfidmobileinventory.domain.models.InventoryItemForScanningModel

data class RfidScannerViewState(
    val isScanningStart: Boolean = false,
    val isReaderInit: Boolean = false,
    val canBackPress: Boolean = true,
    val startScanningButtonVisible: Boolean = false,
    val panelSettingsVisible: Boolean = false,
    val stopScanningButtonVisible: Boolean = false,
    val scannerPowerValue: Int = 50,
    val currentLocation: Int = 0,
    val currentLocationName: String = "",
    val inventoryItems: List<InventoryItemForListModel> = listOf(),
    val inventoryState: InventoryInfoModel = InventoryInfoModel(),
    val inventoryItemsFullRFIDList: List<InventoryItemForScanningModel> = listOf(),
    val scannerType: ReaderType? = null
)
