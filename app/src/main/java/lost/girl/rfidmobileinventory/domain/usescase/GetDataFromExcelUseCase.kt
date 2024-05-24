package lost.girl.rfidmobileinventory.domain.usescase

import android.content.ContentResolver
import android.net.Uri
import android.util.Log
import android.webkit.MimeTypeMap
import org.apache.poi.hssf.usermodel.HSSFDateUtil
import org.apache.poi.hssf.usermodel.HSSFWorkbook
import org.apache.poi.ss.usermodel.Cell
import org.apache.poi.ss.usermodel.FormulaEvaluator
import org.apache.poi.ss.usermodel.Row
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Locale

class GetDataFromExcelUseCase {
   suspend fun execute(
        uri: Uri,
        contentResolver: ContentResolver
    ): Array<Array<String>> {
        var resultArray = arrayOf<Array<String>>()
        try {
            val inputStream = contentResolver.openInputStream(uri)
            Log.d("InvMobRFID", "FileOpen")
            val mime = MimeTypeMap.getSingleton()
            val extension = mime.getExtensionFromMimeType(contentResolver.getType(uri))
            Log.d("InvMobRFID", extension!!)
            val workbook =
                if (extension == "xls") HSSFWorkbook(inputStream) else XSSFWorkbook(inputStream)
            val sheet = workbook.getSheetAt(0)

            val rowsCount = sheet.physicalNumberOfRows
            val formulaEvaluator: FormulaEvaluator =
                workbook.creationHelper.createFormulaEvaluator()
            for (r in 0 until rowsCount) {
                val row: Row = sheet.getRow(r)
                val cellsCount: Int = row.physicalNumberOfCells
                var array = arrayOf<String>()
                for (c in 0 until cellsCount) {
                    val value: String = getCellAsString(row, c, formulaEvaluator)
                    array += value
                }
                resultArray += array
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return resultArray
    }

    private fun getCellAsString(row: Row, c: Int, formulaEvaluator: FormulaEvaluator): String {
        var value = ""
        try {
            val cell = row.getCell(c)
            val cellValue = formulaEvaluator.evaluate(cell)
            when (cellValue.cellType) {
                Cell.CELL_TYPE_BOOLEAN -> value = "" + cellValue.booleanValue
                Cell.CELL_TYPE_NUMERIC -> {
                    val numericValue = cellValue.numberValue
                    value = if (HSSFDateUtil.isCellDateFormatted(cell)) {
                        val date = cellValue.numberValue
                        val formatter = SimpleDateFormat("dd.MM.yyyy", Locale.US)
                        formatter.format(HSSFDateUtil.getJavaDate(date))
                    } else {
                        "" + numericValue
                    }
                }

                Cell.CELL_TYPE_STRING -> value = "" + cellValue.stringValue
                else -> {}
            }
        } catch (e: NullPointerException) {
            e.printStackTrace()
        }
        return value
    }
}