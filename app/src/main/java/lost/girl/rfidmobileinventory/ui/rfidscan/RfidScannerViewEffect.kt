package lost.girl.rfidmobileinventory.ui.rfidscan

sealed class RfidScannerViewEffect {
    data class InventoryReady(val message: Int) : RfidScannerViewEffect()

    data class ShowToast(val message: Int, val errorMessage: Int, val isError: Boolean = false) :
        RfidScannerViewEffect()
}