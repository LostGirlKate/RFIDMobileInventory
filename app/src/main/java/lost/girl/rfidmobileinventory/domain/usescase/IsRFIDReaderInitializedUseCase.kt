package lost.girl.rfidmobileinventory.domain.usescase

import lost.girl.rfidmobileinventory.domain.repository.RFIDReaderRepository

// use case Получение статуса инициализации RFID ридера
class IsRFIDReaderInitializedUseCase(private val repository: RFIDReaderRepository) {
    fun execute(): Boolean {
        return repository.isReaderInitialized()
    }
}
