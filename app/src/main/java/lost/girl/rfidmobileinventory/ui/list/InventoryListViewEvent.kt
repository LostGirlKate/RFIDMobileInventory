package lost.girl.rfidmobileinventory.ui.list

sealed class InventoryListViewEvent {
    data class EditCurrentLocation(val locationID: Int) : InventoryListViewEvent()
}