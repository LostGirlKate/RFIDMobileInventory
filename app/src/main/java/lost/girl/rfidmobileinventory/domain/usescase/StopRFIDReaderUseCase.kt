package lost.girl.rfidmobileinventory.domain.usescase

import lost.girl.rfidmobileinventory.domain.repository.RFIDReaderRepository

class StopRFIDReaderUseCase(private val repository: RFIDReaderRepository) {
    fun execute() {
        repository.stopReader()
    }
}
