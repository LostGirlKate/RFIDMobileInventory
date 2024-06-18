package lost.girl.rfidmobileinventory.domain.usescase

import android.content.Context
import lost.girl.rfidmobileinventory.domain.repository.RFIDReaderRepository

class InitRFIDReaderUseCase(
    private val repository: RFIDReaderRepository,
    private val context: Context
) {
    suspend fun execute(): Boolean {
        return repository.initReader(context)
    }
}
