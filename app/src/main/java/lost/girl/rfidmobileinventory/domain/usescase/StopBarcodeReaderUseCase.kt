package lost.girl.rfidmobileinventory.domain.usescase

import lost.girl.rfidmobileinventory.domain.repository.BarcodeReaderRepository

// use case Остановить 2D сканер
class StopBarcodeReaderUseCase(private val repository: BarcodeReaderRepository) {
    fun execute() {
        repository.stop()
    }
}
