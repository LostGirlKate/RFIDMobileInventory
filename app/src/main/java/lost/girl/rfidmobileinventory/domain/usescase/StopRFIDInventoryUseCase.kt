package lost.girl.rfidmobileinventory.domain.usescase

import lost.girl.rfidmobileinventory.domain.repository.RFIDReaderRepository

// use case Остановаить инвентаризацию(чтение меток) RFID ридером
class StopRFIDInventoryUseCase(private val repository: RFIDReaderRepository) {
    fun execute() {
        repository.stopInventory()
    }
}
