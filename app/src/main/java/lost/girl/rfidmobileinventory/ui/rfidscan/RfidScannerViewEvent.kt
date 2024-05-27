package lost.girl.rfidmobileinventory.ui.rfidscan

sealed class RfidScannerViewEvent {
    data class SetCurrentLocation(val iLocation: Int, val locationName: String) :
        RfidScannerViewEvent()

    data class SetScannerPowerValue(val powerValue: Int) : RfidScannerViewEvent()
    data class SetScanningState(val state: Boolean) : RfidScannerViewEvent()
}