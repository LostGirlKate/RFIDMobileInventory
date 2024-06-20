package lost.girl.rfidmobileinventory.domain.usescase

import lost.girl.rfidmobileinventory.domain.repository.RFIDReaderRepository

// use case Получение можности RFID ридера
class GetRFIDReaderPowerUseCase(private val repository: RFIDReaderRepository) {
    fun execute(): Int {
        return repository.getPower()
    }
}
