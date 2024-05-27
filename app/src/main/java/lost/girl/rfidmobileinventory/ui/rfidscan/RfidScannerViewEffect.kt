package lost.girl.rfidmobileinventory.ui.rfidscan

sealed class RfidScannerViewEffect {
    data class InventoryReady(val message: Int) : RfidScannerViewEffect()
}