package lost.girl.rfidmobileinventory.ui.rfidscan

import lost.girl.rfidmobileinventory.data.readers.ReaderType
import lost.girl.rfidmobileinventory.domain.models.InventoryItemForListModel

sealed class RfidScannerViewEvent {

    // Установить текущее местоположение
    data class SetCurrentLocation(val iLocation: Int, val locationName: String) :
        RfidScannerViewEvent()

    // Установить статус скнирования
    data class SetScanningState(val state: Boolean) : RfidScannerViewEvent()

    // Установить мощность считывателя
    data class SetScanningPower(val power: Int) : RfidScannerViewEvent()

    // Установить тип считывателя
    data class SetScannerType(val type: ReaderType) : RfidScannerViewEvent()

    // Показать AlertDialog для подтверждения действия (onOkClickListener) или подсказки
    data class ShowAlertDialog(val message: Int, val onOkClickListener: () -> Unit) :
        RfidScannerViewEvent()

    // отмена текуущего статуса
    data class ResetInventoryItemState(val item: InventoryItemForListModel) :
        RfidScannerViewEvent()
}
