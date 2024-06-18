package lost.girl.rfidmobileinventory.ui.main

import android.app.AlertDialog

sealed class InventoryMainViewEffect {
    data class ShowToast(val message: Int, val errorMessage: Int, val isError: Boolean = false) :
        InventoryMainViewEffect()

    data class ShowAlertProcessDialog(val message: Int, val processEvent: InventoryMainViewEvent) :
        InventoryMainViewEffect()

    data class HideAlertProcessDialog(val processDialog: AlertDialog?) : InventoryMainViewEffect()

    data class ShowAlertDialog(val message: Int, val onOkClickListener: () -> Unit) :
        InventoryMainViewEffect()

    object OpenExcelFileLauncher : InventoryMainViewEffect()

    object OpenFileManager : InventoryMainViewEffect()
}
