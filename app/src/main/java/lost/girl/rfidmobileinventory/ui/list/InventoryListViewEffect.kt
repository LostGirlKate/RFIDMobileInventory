package lost.girl.rfidmobileinventory.ui.list

sealed class InventoryListViewEffect {
    // Показать AlertDialog для подтверждения действия (onOkClickListener) или подсказки
    data class ShowAlertDialog(val message: Int, val onOkClickListener: () -> Unit) :
        InventoryListViewEffect()
}
