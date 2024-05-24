package lost.girl.rfidmobileinventory.domain.usescase

import android.content.Context
import android.os.Environment
import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import lost.girl.rfidmobileinventory.R
import org.apache.poi.ss.usermodel.Row
import org.apache.poi.ss.util.WorkbookUtil
import org.apache.poi.xssf.streaming.SXSSFWorkbook
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream

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

        val workbook = SXSSFWorkbook()
        val sheet = workbook.createSheet(WorkbookUtil.createSafeSheetName("mysheet"))

        val row: Row = sheet.createRow(0)
        for ((i, item) in columnNames.withIndex()) {
            val cell = row.createCell(i)
            cell.setCellValue(item)
        }

        for ((i, item) in data.withIndex()) {
            val rowData: Row = sheet.createRow(i + 1)
            for (j in item.indices) {
                val cell = rowData.createCell(j)
                cell.setCellValue(item[j])
            }
        }
        Log.d("InvMobRFID", "Start write export file")

        return try {
            val outFile = File(
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),
                fileName
            )
            withContext(Dispatchers.IO) {
                val outputStream: OutputStream = FileOutputStream(outFile.absolutePath)
                workbook.write(outputStream)
                outputStream.flush()
                outputStream.close()
                Log.d("InvMobRFID", "Ready export file")
            }
            true
        } catch (e: Exception) {
            Log.d("InvMobRFID", e.toString())
            false
        }
    }
}