package lost.girl.rfidmobileinventory.domain.usescase

import lost.girl.rfidmobileinventory.R
import lost.girl.rfidmobileinventory.utils.ExcelUtil
import lost.girl.rfidmobileinventory.utils.ResourcesProvider

// use case Выгрузить данные в Excel файл
class ExportDataToExcelFileUseCase(private val resourcesProvider: ResourcesProvider) {
    suspend fun execute(
        data: List<List<String>>,
        fileName: String,
    ): Boolean {
        val columnNames = listOf(
            resourcesProvider.getString(R.string.num),
            resourcesProvider.getString(R.string.inventory_num),
            resourcesProvider.getString(R.string.manager_name),
            resourcesProvider.getString(R.string.location),
            resourcesProvider.getString(R.string.type),
            resourcesProvider.getString(R.string.model),
            resourcesProvider.getString(R.string.serial_num),
            resourcesProvider.getString(R.string.shipment_num),
            resourcesProvider.getString(R.string.rfid_dec),
            resourcesProvider.getString(R.string.location_fact)
        )
        return ExcelUtil.exportDataToExcelFile(data, fileName, columnNames)
    }
}
