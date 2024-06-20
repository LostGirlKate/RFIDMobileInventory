package lost.girl.rfidmobileinventory.ui.main

import android.app.AlertDialog
import android.content.ContentResolver
import android.net.Uri

sealed class InventoryMainViewEvent {
    // Загрузка данных из файла
    data class LoadDataFromFile(
        val uri: Uri,
        val contentResolver: ContentResolver,
        val processDialog: AlertDialog? = null,
    ) : InventoryMainViewEvent()

    // Открыть окно для выбора файла для загрузки
    object OpenFileManager : InventoryMainViewEvent()

    // Обновить даннные о состоянии инвентаризации
    object RefreshData : InventoryMainViewEvent()

    // Показать ProcessDialog при выполнении долгих расчетов, в параметр передаем Event,
    // который запустится после начала отображения ProcessDialog
    data class ShowProcessDialog(val message: Int, val processEvent: InventoryMainViewEvent) :
        InventoryMainViewEvent()

    // Сохранить данные в файл
    data class SaveDataToFile(
        val processDialog: AlertDialog? = null,
    ) : InventoryMainViewEvent()

    // Завершить инвентаризацию (выгрузить данные в Excel и очистить BD)
    data class CloseInventory(val processDialog: AlertDialog? = null) : InventoryMainViewEvent()

    // Показать AlertDialog для подтверждения действия (onOkClickListener) или подсказки
    data class ShowAlertDialog(val message: Int, val onOkClickListener: () -> Unit) :
        InventoryMainViewEvent()
}
