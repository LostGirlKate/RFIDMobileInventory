package lost.girl.rfidmobileinventory.domain.usescase

import android.content.Context
import lost.girl.rfidmobileinventory.domain.repository.BarcodeReaderRepository

class OpenBarcodeReaderUseCase(
    private val repository: BarcodeReaderRepository,
    private val context: Context
) {
    suspend fun execute(onSuccess: (String) -> Unit): Boolean {
        return repository.open(context, onSuccess)
    }
}