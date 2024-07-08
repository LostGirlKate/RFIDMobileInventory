package lost.girl.rfidmobileinventory.ui.list

import lost.girl.rfidmobileinventory.domain.models.InventoryItemForListModel

sealed class InventoryListViewEvent {
    // Изменение фильтра по местоположению
    data class EditCurrentLocation(val locationID: Int) : InventoryListViewEvent()

    // Показать AlertDialog для подтверждения действия (onOkClickListener) или подсказки
    data class ShowAlertDialog(val message: Int, val onOkClickListener: () -> Unit) :
        InventoryListViewEvent()

    // отмена текуущего статуса
    data class ResetInventoryItemState(val item: InventoryItemForListModel) :
        InventoryListViewEvent()
}
