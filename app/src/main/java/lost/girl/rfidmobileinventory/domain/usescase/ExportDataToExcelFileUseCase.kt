package lost.girl.rfidmobileinventory.domain.usescase

import android.content.Context
import lost.girl.rfidmobileinventory.R
import lost.girl.rfidmobileinventory.utils.ExcelUtil

class ExportDataToExcelFileUseCase(private val context: Context) {
    suspend fun execute(
        data: List<List<String>>,
        fileName: String
    ): Boolean {
        val columnNames = listOf(
            context.getString(R.string.num),
            context.getString(R.string.inventory_num),
            context.getString(R.string.manager_name),
            context.getString(R.string.location),
            context.getString(R.string.type),
            context.getString(R.string.model),
            context.getString(R.string.serial_num),
            context.getString(R.string.shipment_num),
            context.getString(R.string.rfid_dec),
            context.getString(R.string.location_fact)
        )
        return ExcelUtil.exportDataToExcelFile(data, fileName, columnNames)
    }
}
