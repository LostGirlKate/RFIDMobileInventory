package lost.girl.rfidmobileinventory.domain.usescase

import android.content.ContentResolver
import android.net.Uri
import lost.girl.rfidmobileinventory.utils.ExcelUtil

// use case Получение массива данных из Excel файла
class GetDataFromExcelUseCase {
    suspend fun execute(
        uri: Uri,
        contentResolver: ContentResolver,
    ): Array<Array<String>> {
        return ExcelUtil.getDataFromExcel(uri, contentResolver)
    }
}
