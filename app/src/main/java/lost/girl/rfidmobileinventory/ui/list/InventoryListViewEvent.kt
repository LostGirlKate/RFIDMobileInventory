package lost.girl.rfidmobileinventory.ui.list

sealed class InventoryListViewEvent {
    // Изменение фильтра по местоположению
    data class EditCurrentLocation(val locationID: Int) : InventoryListViewEvent()
}
