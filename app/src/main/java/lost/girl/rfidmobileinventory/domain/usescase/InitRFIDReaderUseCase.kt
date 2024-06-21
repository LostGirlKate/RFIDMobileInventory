package lost.girl.rfidmobileinventory.domain.usescase

import lost.girl.rfidmobileinventory.data.readers.rfid.IRfidReader

// use case Инициализация RFID ридера
class InitRFIDReaderUseCase(
    private val reader: IRfidReader,
) {
    suspend fun execute(): Boolean {
        return reader.isReaderInitialized
    }
}
