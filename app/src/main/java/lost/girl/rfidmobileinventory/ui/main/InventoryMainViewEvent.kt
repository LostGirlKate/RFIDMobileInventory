package lost.girl.rfidmobileinventory.ui.main

import android.app.AlertDialog
import android.content.ContentResolver
import android.net.Uri

sealed class InventoryMainViewEvent {
    data class LoadDataFromFile(
        val uri: Uri,
        val contentResolver: ContentResolver,
        val processDialog: AlertDialog? = null
    ) : InventoryMainViewEvent()

    object OpenFileManager : InventoryMainViewEvent()
    object RefreshData : InventoryMainViewEvent()

    data class ShowProcessDialog(val message: Int, val processEvent: InventoryMainViewEvent) :
        InventoryMainViewEvent()

    data class SaveDataToFile(
        val processDialog: AlertDialog? = null
    ) : InventoryMainViewEvent()

    data class CloseInventory(val processDialog: AlertDialog? = null) : InventoryMainViewEvent()

    data class ShowAlertDialog(val message: Int, val onOkClickListener: () -> Unit) :
        InventoryMainViewEvent()
}
