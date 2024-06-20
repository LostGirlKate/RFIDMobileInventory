package lost.girl.rfidmobileinventory.domain.usescase

import lost.girl.rfidmobileinventory.domain.repository.BarcodeReaderRepository

// use case Начать сканирование 2D сканером
class StartBarcodeReaderUseCase(private val repository: BarcodeReaderRepository) {
    fun execute(): Boolean {
        return repository.start()
    }
}
