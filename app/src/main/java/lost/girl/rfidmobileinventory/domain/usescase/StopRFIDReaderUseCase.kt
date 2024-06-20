package lost.girl.rfidmobileinventory.domain.usescase

import lost.girl.rfidmobileinventory.domain.repository.RFIDReaderRepository

// use case Остановить RFID ридер
class StopRFIDReaderUseCase(private val repository: RFIDReaderRepository) {
    fun execute() {
        repository.stopReader()
    }
}
