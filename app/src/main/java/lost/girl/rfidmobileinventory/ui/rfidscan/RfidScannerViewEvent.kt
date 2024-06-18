package lost.girl.rfidmobileinventory.ui.rfidscan

import lost.girl.rfidmobileinventory.data.readers.ReaderType

sealed class RfidScannerViewEvent {
    data class SetCurrentLocation(val iLocation: Int, val locationName: String) :
        RfidScannerViewEvent()

    data class SetScanningState(val state: Boolean) : RfidScannerViewEvent()
    data class SetScanningPower(val power: Int) : RfidScannerViewEvent()
    data class SetScannerType(val type: ReaderType) : RfidScannerViewEvent()
}
