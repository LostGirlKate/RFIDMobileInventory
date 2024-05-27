package lost.girl.rfidmobileinventory.ui.main

import android.app.AlertDialog
import android.app.Application
import android.content.ContentResolver
import android.net.Uri
import android.util.Log
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import lost.girl.rfidmobileinventory.R
import lost.girl.rfidmobileinventory.domain.usescase.ClearDataBaseUseCase
import lost.girl.rfidmobileinventory.domain.usescase.ExportDataToExcelFileUseCase
import lost.girl.rfidmobileinventory.domain.usescase.GetDataForExcelUseCase
import lost.girl.rfidmobileinventory.domain.usescase.GetDataFromExcelUseCase
import lost.girl.rfidmobileinventory.domain.usescase.GetInventoryInfoUseCase
import lost.girl.rfidmobileinventory.domain.usescase.LoadDataToDataBaseUseCase
import lost.girl.rfidmobileinventory.mvi.MviViewModel
import java.text.SimpleDateFormat
import java.util.Locale

class InventoryMainViewModel(
    application: Application,
    private val getInventoryInfo: GetInventoryInfoUseCase,
    private val loadDataToDataBaseUseCase: LoadDataToDataBaseUseCase,
    private val getDataForExcelUseCase: GetDataForExcelUseCase,
    private val clearDataBaseUseCase: ClearDataBaseUseCase,
    private val getDataFromExcelUseCase: GetDataFromExcelUseCase,
    private val exportDataToExcelFileUseCase: ExportDataToExcelFileUseCase
) :
    MviViewModel<InventoryMainViewState, InventoryMainViewEffect, InventoryMainViewEvent>(
        application
    ) {

    init {
        viewState = InventoryMainViewState(getInventoryInfo.execute())
    }

    override fun process(viewEvent: InventoryMainViewEvent) {
        super.process(viewEvent)
        when (viewEvent) {
            InventoryMainViewEvent.OpenFileManager -> openFileManager()

            is InventoryMainViewEvent.LoadDataFromFile -> loadDataFromExcel(
                viewEvent.uri,
                viewEvent.contentResolver,
                viewEvent.processDialog
            )

            is InventoryMainViewEvent.SaveDataToFile -> exportDataToExcel(
                viewEvent.processDialog
            )

            is InventoryMainViewEvent.CloseInventory -> closeInventory(viewEvent.processDialog)

            is InventoryMainViewEvent.ShowProcessDialog -> showProcessDialog(
                viewEvent.message,
                viewEvent.processEvent
            )

            is InventoryMainViewEvent.ShowAlertDialog -> showAlertDialog(
                viewEvent.message,
                viewEvent.onOkClickListener
            )

            InventoryMainViewEvent.RefreshData -> refreshInventoryState()
        }

    }

    private fun openFileManager() {
        viewEffect = InventoryMainViewEffect.OpenFileManager
    }

    private fun showProcessDialog(message: Int, processEvent: InventoryMainViewEvent) {
        viewEffect = InventoryMainViewEffect.ShowAlertProcessDialog(message, processEvent)
    }

    private fun showAlertDialog(message: Int, onOkClickListener: () -> Unit) {
        viewEffect = InventoryMainViewEffect.ShowAlertDialog(message, onOkClickListener)
    }

    private fun loadDataFromExcel(
        uri: Uri,
        contentResolver: ContentResolver,
        processDialog: AlertDialog?
    ) {
        viewModelScope.launch {
            delay(1000)
            val dataArray =
                getDataFromExcelUseCase.execute(
                    uri,
                    contentResolver
                )
            val parseResult = parseDataToDataBase(dataArray) && dataArray.isNotEmpty()
            viewEffect = InventoryMainViewEffect.HideAlertProcessDialog(processDialog)
            viewEffect = InventoryMainViewEffect.ShowToast(
                R.string.data_load_message, R.string.data_error_load_message,
                !parseResult
            )
        }

    }

    private fun exportDataToExcel(
        processDialog: AlertDialog?
    ) {
        viewModelScope.launch {
            Log.d("InvMobRFID", "Start export file")
            delay(1000)
            val loadResult = exportData()
            viewEffect = InventoryMainViewEffect.HideAlertProcessDialog(processDialog)
            viewEffect = InventoryMainViewEffect.ShowToast(
                R.string.file_save_message, R.string.file_error_save_message,
                !loadResult
            )
        }
    }

    private fun refreshInventoryState() {
        viewState = viewState.copy(inventoryState = getInventoryInfo.execute())
    }

    private fun closeInventory(processDialog: AlertDialog?) {
        viewModelScope.launch {
            var totalResult = true
            var actionResult = exportData()
            if (!actionResult) totalResult = false
            actionResult = clearDataBaseUseCase.execute()
            if (!actionResult) totalResult = false
            delay(2000)
            refreshInventoryState()
            Log.d("InvMobRFID", "totalResult $totalResult")
            viewEffect = InventoryMainViewEffect.HideAlertProcessDialog(processDialog)
            viewEffect = InventoryMainViewEffect.ShowToast(
                R.string.data_save_message, R.string.data_error_save_message,
                !totalResult
            )
        }
    }


    private suspend fun exportData(): Boolean {
        val dateFormat = SimpleDateFormat("dd.MM.yyyy HH.mm", Locale.US)
        val fileName = "inventory_${dateFormat.format(System.currentTimeMillis())}.xlsx"
        val data = getDataForExcelUseCase.execute()
        return exportDataToExcelFileUseCase.execute(
            data = data,
            fileName = fileName
        )
    }

    private suspend fun parseDataToDataBase(data: Array<Array<String>>): Boolean {
        val result = loadDataToDataBaseUseCase.execute(data)
        viewState = viewState.copy(inventoryState = getInventoryInfo.execute())
        return result
    }
}