package lost.girl.rfidmobileinventory.domain.usescase

import android.content.Context
import lost.girl.rfidmobileinventory.domain.repository.BarcodeReaderRepository

// use case Открытие(инициализация) 2D сканера
class OpenBarcodeReaderUseCase(
    private val repository: BarcodeReaderRepository,
    private val context: Context,
) {
    suspend fun execute(onSuccess: (String) -> Unit): Boolean {
        return repository.open(context, onSuccess)
    }
}
