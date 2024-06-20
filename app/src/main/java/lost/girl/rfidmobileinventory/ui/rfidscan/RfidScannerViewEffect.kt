package lost.girl.rfidmobileinventory.ui.rfidscan

sealed class RfidScannerViewEffect {
    // Показать оповещение что все метки в даннном местоположении найдены
    data class InventoryReady(val message: Int) : RfidScannerViewEffect()

    // Показать Toast
    data class ShowToast(val message: Int, val errorMessage: Int, val isError: Boolean = false) :
        RfidScannerViewEffect()
}
