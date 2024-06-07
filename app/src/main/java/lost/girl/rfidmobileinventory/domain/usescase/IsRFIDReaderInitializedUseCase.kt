package lost.girl.rfidmobileinventory.domain.usescase

import lost.girl.rfidmobileinventory.domain.repository.RFIDReaderRepository

class IsRFIDReaderInitializedUseCase(private val repository: RFIDReaderRepository) {
    fun execute(): Boolean {
        return repository.isReaderInitialized()
    }
}